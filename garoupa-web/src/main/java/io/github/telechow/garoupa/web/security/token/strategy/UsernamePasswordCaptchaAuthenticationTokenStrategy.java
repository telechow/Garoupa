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
package io.github.telechow.garoupa.web.security.token.strategy;

import io.github.telechow.garoupa.web.security.details.GaroupaWebAuthenticationDetails;
import io.github.telechow.garoupa.web.security.token.UsernamePasswordCaptchaAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * 用户名密码验证码认证token策略
 *
 * @author Telechow
 * @since 2023/4/10 11:28
 */
@Component
public class UsernamePasswordCaptchaAuthenticationTokenStrategy implements IAuthenticationTokenStrategy {

    public static final String LOGIN_MODE = "username-password-captcha";

    @Override
    public void afterPropertiesSet() {
        AuthenticationTokenStrategyFactory.register(LOGIN_MODE, this);

    }

    @Override
    public AbstractAuthenticationToken instanceAuthenticationToken(GaroupaWebAuthenticationDetails details) {
        return UsernamePasswordCaptchaAuthenticationToken.unauthenticated(
                details.getUsername(), details.getPassword(), details.getCaptchaKey(), details.getCaptchaCode());
    }
}
