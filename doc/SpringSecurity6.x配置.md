# SpringSecurity6.x配置

---

SpringBoot3.x使用的是SpringSecurity6.x，配置和代码比起之前的版本有较大变动。

SpringSecurity较为复杂，具体原理本文不去阐述，只写具体用法和一些需要注意的点。代码也只截取重要的部分。

Garoupa使用`Jwt`作无状态或有状态的认证和授权，将会大量使用Redis来提升速度。其中jwt的生成和解析使用Hutool工具包。

## 引入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!--Hutool-->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-jwt</artifactId>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-captcha</artifactId>
</dependency>
```

## 认证

认证即我们常说的登录，是让系统知道你是谁的一个过程。

### 用户没有登录时，进行身份认证

改造SpringSecurity实现认证的方法很多，网上也可以搜到很多教程。这里简单说一下我的实现方式，完成多种方式的认证，比如：用户名密码验证码，邮箱地址邮件验证码，手机号短信验证码，等等。

- 首先SpringSecurity5.7开始，继承`WebSecurityConfigurerAdapter`配置过滤器链的方式就过时了。现在推荐直接注入`SecurityFilterChain`的Bean来完成配置。我们直接禁用SpringSecurity的formLogin配置。

```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //禁用默认的登录页面及其配置
                .formLogin(AbstractHttpConfigurer::disable)
                //其他配置略
                .build();
    }
```

- 我们不准备使用`UsernamePasswordAuthenticationFilter`进行认证，我们自定义一个过滤器，继承`AbstractAuthenticationProcessingFilter`，在其中解析请求中的参数和请求头，构造对应登录方式的认证token。过滤器的监听uri为`/garoupa/login`，和SpringSecurity默认的`/login`不一样

- 解析参数和请求头，我们使用`WebAuthenticationDetails`的子类，`AuthenticationDetailsSource`的实现类来实现。在过滤器的中使用`this.authenticationDetailsSource.buildDetails(request);`就可以获得这些数据。

- 需要多少种认证方式，就编写多少个认证token，继承`AbstractAuthenticationToken`类。参考`UsernamePasswordAuthenticationToken`来编写。

- 需要多少种认证方式，就编写多少个`AuthenticationProvider`的实现类，在其中进行校验参数，匹配密码等等业务逻辑。所有业务逻辑校验无误，即可确定认证token，返回`AuthenticationToken`其中包含了用户信息，权限信息。

- 编写过滤器的成功处理器和失败处理器，并在注入自定义过滤器Bean的时候设置到其中。过滤器的认证成功处理器中需要，生成该用户的jwt，在响应头中返回的用户；同时还要将一些不敏感的数据，如用户名、头像、拥有的资源等响应给用户。
  
  ```java
  package io.github.telechow.garoupa.web.security.handler;
  
  import cn.hutool.core.bean.BeanUtil;
  import cn.hutool.jwt.JWTPayload;
  import cn.hutool.jwt.JWTUtil;
  import com.alibaba.fastjson2.JSON;
  import io.github.telechow.garoupa.api.vo.ResponseResult;
  import io.github.telechow.garoupa.api.vo.login.LoginVo;
  import io.github.telechow.garoupa.web.security.domain.GaroupaUser;
  import io.github.telechow.garoupa.web.security.properties.JwtProperties;
  import jakarta.servlet.http.HttpServletRequest;
  import jakarta.servlet.http.HttpServletResponse;
  import lombok.RequiredArgsConstructor;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.http.HttpHeaders;
  import org.springframework.http.HttpStatus;
  import org.springframework.http.MediaType;
  import org.springframework.security.core.Authentication;
  import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
  import org.springframework.stereotype.Component;
  
  import java.io.IOException;
  import java.nio.charset.StandardCharsets;
  import java.time.LocalDateTime;
  import java.time.ZoneId;
  import java.util.Map;
  
  /**
   * Garoupa认证成功处理器
   *
   * @author Telechow
   * @since 2023/3/30 12:52
   */
  @Component
  @RequiredArgsConstructor(onConstructor_ = @Autowired)
  public class GaroupaAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  
      private final JwtProperties jwtProperties;
  
      @Override
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
              , Authentication authentication) throws IOException {
          //1.设置响应Content-Type和字符集和http状态码
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.setCharacterEncoding(StandardCharsets.UTF_8.name());
          response.setStatus(HttpStatus.OK.value());
  
          //2.生成jwt，并设置在响应头Authorization中
          GaroupaUser garoupaUser = (GaroupaUser) authentication.getPrincipal();
          Map<String, Object> payloadMap = Map.of(JWTPayload.EXPIRES_AT
                  , LocalDateTime.now().plusDays(30L).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                  , JWTPayload.SUBJECT, garoupaUser.getUser().getId());
          String jwt = JWTUtil.createToken(payloadMap, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
          response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
  
          //3.将用户数据设置在响应体中
          LoginVo vo = new LoginVo();
          BeanUtil.copyProperties(garoupaUser.getUser(), vo, false);
          vo.setResourceCodeList(garoupaUser.getResources());
          ResponseResult<LoginVo> responseResult = ResponseResult.data(vo);
          response.getWriter().write(JSON.toJSONString(responseResult));
      }
  }
  ```

- 配置过滤器链
  
  ```java
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                  //禁用csrf，因为我们是前后端分离的，每次调用接口都需要使用token鉴权，所以无需csrf功能
                  .csrf().disable()
                  //不使用Session获取SecurityContext
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                  //禁用默认的登录页面及其配置
                  .formLogin(AbstractHttpConfigurer::disable)
                  //配置用户登录过滤器
                  .addFilterAt(garoupaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                  //其他配置，略
                  .build();
      }
  ```

### 用户已经成功登录，进行身份认证

用户已经获得了jwt，每次访问接口时携带jwt在请求头`Authorization`中，即表明了其身份。我们应该如何识别其身份呢。

- 继承`OncePerRequestFilter`编写一个jwt解析的过滤器，其中主要负责解析jwt，查询jwt对应用户的数据和权限（一般要加入缓存）,构造`PreAuthenticatedAuthenticationToken`设置到`SecurityContextHolder`上下文中。

- 配置此过滤器到过滤器链中
  
  ```java
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                  //配置用户登录过滤器
                  .addFilterAt(garoupaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                  .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                  //其他配置，略
                  .build();
      }
  ```

## 退出登录

我们是使用jwt，退出登录只需要给用户一个友好提示即可。不需要做其他事情。如果jwt有状态化，则需要移除服务器端的jwt。

- 实现`LogoutSuccessHandler`，给用户一个友好的响应

- 配置到过滤器链中
  
  ```java
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                  //退出登录配置
                  .logout(customizer -> customizer
                          .logoutUrl("/garoupa/logout")
                          .logoutSuccessHandler(garoupaLogoutSuccessHandler))
                  //其他配置，略
                  .build();
      }
  ```

## 鉴权

我们使用rbac模型来实现鉴权，并将资源分为两种，权限、资源（菜单和按钮）来分别管理，前者不会返回给前端，后者将会在认证之后响应给前端，供前端项目控制菜单展示使用。

- 配置哪些接口可以随意访问，哪些接口只能匿名访问，哪些接口只能认证后访问
  
  ```java
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                  //配置鉴权策略
                  .authorizeHttpRequests(authorize -> authorize
                          //可以随意访问的接口
                          .requestMatchers(authorizeProperties.getPermitAllUri()).permitAll()
                          //登录相关接口，只有匿名时才可以访问（即未登录是可以访问，登录之后不能访问）
                          .requestMatchers(authorizeProperties.getAnonymousUri()).anonymous()
                          .requestMatchers(authorizeProperties.getDenyAllUri()).denyAll()
                          //其他的接口都需要登录才能访问
                          .anyRequest().authenticated()
                  )
                  .build();
      }
  ```

- 开启方法安全注解`@EnableMethodSecurity`

- 在接口上添加注解，`@PreAuthoriz`，通过el表达式的结果布尔值，确定拥有哪些权限的用户可以访问
  
  ```java
      @GetMapping("/show")
      @PreAuthorize("@garoupaExpressionRoot.hasAuthority('super:all:all')")
      public ResponseResult<String> show(@RequestParam(name = "text") String text) {
          return ResponseResult.data(indexService.show(text));
      }
  ```

## 自定义权限标识符通配符匹配

```java
package io.github.telechow.garoupa.web.security.expression.root;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.text.CharPool;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * Garoupa表达式根
 * <li>支持通配符*匹配</li>
 * <li>权限标识符形如:a:b:c,则支持a:*:*,a:b:*,a:**这样的匹配</li>
 * <li>使用{@link AntPathMatcher}进行通配符匹配，并使用半角冒号:作为分隔符，?代表一个字符，*号代表零个或多个字符，**代表任意个分隔符中的任意个字符</li>
 *
 * @author Telechow
 * @since 2023/4/4 14:52
 */
@Component
public class GaroupaExpressionRoot {

    /**
     * 使用":"作为分隔符的通配符匹配
     */
    private final AntPathMatcher antPathMatcher = new AntPathMatcher(String.valueOf(CharPool.COLON));

    /**
     * 是否拥有某个权限
     * <li>允许通配符匹配</li>
     *
     * @param authority 权限标识符
     * @return boolean 是否允许访问
     * @author Telechow
     * @since 2023/4/4 14:53
     */
    public final boolean hasAuthority(String authority) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (antPathMatcher.match(grantedAuthority.getAuthority(), authority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否拥有多个权限中的任意一个
     * <li>允许通配符匹配</li>
     *
     * @param authorities 权限标识符数组
     * @return boolean 是否允许访问
     * @author Telechow
     * @since 2023/4/4 15:29
     */
    public final boolean hasAnyAuthorities(String... authorities) {
        if (CollectionUtil.isEmpty(List.of(authorities))) {
            return false;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            for (String authority : authorities) {
                if (antPathMatcher.match(grantedAuthority.getAuthority(), authority)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否拥有多个权限中的所有权限
     * <li>允许通配符匹配</li>
     *
     * @param authorities 权限标识符数组
     * @return boolean 是否允许访问
     * @author Telechow
     * @since 2023/4/4 15:31
     */
    public final boolean hasAllAuthorities(String... authorities) {
        if (CollectionUtil.isEmpty(List.of(authorities))) {
            return false;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        for (String authority : authorities) {
            boolean flag = false;
            for (GrantedAuthority grantedAuthority : grantedAuthorities) {
                if (antPathMatcher.match(grantedAuthority.getAuthority(), authority)) {
                    flag = true;
                }
            }
            if (!flag) {
                return false;
            }
        }
        return true;
    }

}
```



## 防XXS安全配置

SpringSecurity提供了非常简单的配置

```java
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //防XSS安全配置
                .headers(HeadersConfigurer::xssProtection)
                //其他配置，略
                .build();
    }
```
