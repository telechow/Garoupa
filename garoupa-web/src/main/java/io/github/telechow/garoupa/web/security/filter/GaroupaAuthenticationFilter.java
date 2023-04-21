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

import io.github.telechow.garoupa.web.security.details.GaroupaWebAuthenticationDetails;
import io.github.telechow.garoupa.web.security.token.strategy.AuthenticationTokenStrategyFactory;
import io.github.telechow.garoupa.web.security.token.strategy.IAuthenticationTokenStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Optional;

/**
 * Garoupa认证过滤器
 *
 * @author Telechow
 * @since 2023/3/29 12:51
 */
public class GaroupaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * Garoupa认证过滤器的构造方法
     *
     * @param authenticationManager        认证管理器
     * @param authenticationDetailsSource  认证详细信息源
     * @param authenticationSuccessHandler 认证成功处理器
     * @param authenticationFailureHandler 认证失败处理器
     * @param loginUri                     登录uri
     * @author Telechow
     * @since 2023/4/3 15:52
     */
    public GaroupaAuthenticationFilter(AuthenticationManager authenticationManager
            , AuthenticationDetailsSource<HttpServletRequest, GaroupaWebAuthenticationDetails> authenticationDetailsSource
            , AuthenticationSuccessHandler authenticationSuccessHandler
            , AuthenticationFailureHandler authenticationFailureHandler, String loginUri) {
        super(loginUri, authenticationManager);
        this.setAuthenticationDetailsSource(authenticationDetailsSource);
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //1.只支持Post请求
        if (!HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        //2.获取请求详细信息
        GaroupaWebAuthenticationDetails details = (GaroupaWebAuthenticationDetails)
                this.authenticationDetailsSource.buildDetails(request);

        //3.根据登录方式，并进行不同的操作
        IAuthenticationTokenStrategy strategy = AuthenticationTokenStrategyFactory.getStrategy(details.getLoginMode());
        Optional.ofNullable(strategy).orElseThrow(() -> new CredentialsExpiredException("not supported login mode."));
        return this.getAuthenticationManager().authenticate(strategy.instanceAuthenticationToken(details));
    }

}
