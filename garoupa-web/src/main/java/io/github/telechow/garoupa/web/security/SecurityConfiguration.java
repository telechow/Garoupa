/**
 * Copyright 2023 telechow
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.telechow.garoupa.web.security;

import io.github.telechow.garoupa.web.security.properties.AuthenticationProperties;
import io.github.telechow.garoupa.web.security.properties.AuthorizeProperties;
import io.github.telechow.garoupa.web.security.details.source.GaroupaAuthenticationDetailsSource;
import io.github.telechow.garoupa.web.security.filter.GaroupaAuthenticationFilter;
import io.github.telechow.garoupa.web.security.filter.JwtAuthenticationTokenFilter;
import io.github.telechow.garoupa.web.security.handler.GaroupaAuthenticationFailureHandler;
import io.github.telechow.garoupa.web.security.handler.GaroupaAuthenticationSuccessHandler;
import io.github.telechow.garoupa.web.security.handler.GaroupaLogoutSuccessHandler;
import io.github.telechow.garoupa.web.security.provider.EmailAddressEmailCodeAuthenticationProvider;
import io.github.telechow.garoupa.web.security.provider.UsernamePasswordCaptchaAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * SpringSecurity过滤器链配置
 *
 * @author Telechow
 * @since 2023/3/29 13:10
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    private final GaroupaAuthenticationSuccessHandler garoupaAuthenticationSuccessHandler;
    private final GaroupaAuthenticationFailureHandler garoupaAuthenticationFailureHandler;
    private final GaroupaLogoutSuccessHandler garoupaLogoutSuccessHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    private final GaroupaAuthenticationDetailsSource garoupaAuthenticationDetailsSource;

    private final UsernamePasswordCaptchaAuthenticationProvider usernamePasswordCaptchaAuthenticationProvider;
    private final EmailAddressEmailCodeAuthenticationProvider emailAddressEmailCodeAuthenticationProvider;

    private final CorsConfigurationSource corsConfigurationSource;

    private final AuthorizeProperties authorizeProperties;
    private final AuthenticationProperties authenticationProperties;

    /**
     * 认证管理器的Bean，认证管理器实际上是一个认证提供者管理器
     * <li>向其中加入自定的认证提供者</li>
     *
     * @return org.springframework.security.authentication.AuthenticationManager 认证管理器的Bean
     * @author Telechow
     * @since 2023/4/3 15:50
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(usernamePasswordCaptchaAuthenticationProvider
                , emailAddressEmailCodeAuthenticationProvider);
    }

    /**
     * Security过滤器链配置的Bean
     *
     * @param http HttpSecurity对象
     * @return org.springframework.security.web.SecurityFilterChain Security过滤器链配置的Bean
     * @author Telechow
     * @since 2023/3/29 11:27
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                //禁用csrf，因为我们是前后端分离的，每次调用接口都需要使用token鉴权，所以无需csrf功能
                .csrf().disable()
                //不使用Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
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
                //禁用默认的登录页面及其配置
                .formLogin(AbstractHttpConfigurer::disable)
                //退出登录配置
                .logout(customizer -> customizer
                        .logoutUrl(authenticationProperties.getLogoutUri())
                        .logoutSuccessHandler(garoupaLogoutSuccessHandler))
                //配置用户登录过滤器
                .addFilterAt(garoupaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //异常处理器
                .exceptionHandling(customizer -> customizer
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                //跨域配置
                .cors(customizer -> customizer.configurationSource(corsConfigurationSource))
                //防XSS安全配置
                .headers(HeadersConfigurer::xssProtection)
                .build();
    }

    /**
     * Garoupa认证过滤器的Bean
     *
     * @return io.github.telechow.garoupa.web.security.filter.GaroupaAuthenticationFilter Garoupa认证过滤器的Bean
     * @author Telechow
     * @since 2023/3/30 21:46
     */
    @Bean
    public GaroupaAuthenticationFilter garoupaAuthenticationFilter() {
        return new GaroupaAuthenticationFilter(authenticationManager(), garoupaAuthenticationDetailsSource
                , garoupaAuthenticationSuccessHandler, garoupaAuthenticationFailureHandler
                , authenticationProperties.getLoginUri());
    }
}
