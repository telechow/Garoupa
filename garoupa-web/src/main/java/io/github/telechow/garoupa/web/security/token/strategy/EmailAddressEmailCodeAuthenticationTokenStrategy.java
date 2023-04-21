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
import io.github.telechow.garoupa.web.security.token.EmailAddressEmailCodeAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * 邮箱地址邮件验证码认证token策略
 *
 * @author Telechow
 * @since 2023/4/10 13:27
 */
@Component
public class EmailAddressEmailCodeAuthenticationTokenStrategy implements IAuthenticationTokenStrategy {

    public static final String LOGIN_MODE = "email-address-email-code";

    @Override
    public void afterPropertiesSet() {
        AuthenticationTokenStrategyFactory.register(LOGIN_MODE, this);
    }

    @Override
    public AbstractAuthenticationToken instanceAuthenticationToken(GaroupaWebAuthenticationDetails details) {
        return EmailAddressEmailCodeAuthenticationToken.unauthenticated(
                details.getEmailAddress(), details.getEmailCode());
    }


}
