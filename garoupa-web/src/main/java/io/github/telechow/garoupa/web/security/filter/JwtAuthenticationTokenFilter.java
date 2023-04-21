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
package io.github.telechow.garoupa.web.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.api.entity.Resource;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import io.github.telechow.garoupa.web.auto.service.IResourceAutoService;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import io.github.telechow.garoupa.web.security.properties.JwtProperties;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * jwt认证token过滤器
 *
 * @author Telechow
 * @since 2023/4/3 20:02
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final IUserAutoService userAutoService;
    private final IPermissionAutoService permissionAutoService;
    private final IResourceAutoService resourceAutoService;

    private final JwtProperties jwtProperties;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response
            , @Nonnull FilterChain filterChain) throws ServletException, IOException {
        try {
            //1.从请求头中获取token
            String token = request.getHeader(HttpHeaders.AUTHORIZATION);
            //1.1.如果获取不到token，放行并返回
            if (StrUtil.isBlank(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            //2.验证token，并解析token
            //2.1.验证token，如果验证失败，抛出异常
            final boolean verifyError = !JWTUtil.verify(token, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
            if (verifyError) {
                throw new BadCredentialsException(ResponseCode.AUTHENTICATION_TOKEN_ILLEGAL.getMsg());
            }
            //2.2.解析token，获取用户id
            JWT jwt = JWTUtil.parseToken(token);
            Long userId = Long.valueOf(jwt.getPayload(JWTPayload.SUBJECT).toString());

            //3.获取用户信息
            User user = userAutoService.getUerByIdPutCache(userId);
            //3.1.如果用户实体不存在，抛出异常
            Optional.ofNullable(user)
                    .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.AUTHENTICATION_TOKEN_SUBJECT_NOT_EXIST.getMsg()));
            //3.2.查询用户拥有的权限和资源
            List<Permission> permissions = permissionAutoService.listPermissionByUserIdPutCache(userId);
            List<String> permissionCodeList = permissions.stream()
                    .map(Permission::getPermissionCode)
                    .toList();
            List<Resource> resources = resourceAutoService.listResourceByUserIdPutCache(userId);
            List<String> resourceCodeList = resources.stream()
                    .map(Resource::getResourceCode)
                    .toList();
            GaroupaUser garoupaUser = new GaroupaUser(user, permissionCodeList, resourceCodeList);

            //4.构造token，传入SecurityContextHolder中，然后放行
            PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken
                    (garoupaUser, token, garoupaUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            this.authenticationEntryPoint.commence(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
