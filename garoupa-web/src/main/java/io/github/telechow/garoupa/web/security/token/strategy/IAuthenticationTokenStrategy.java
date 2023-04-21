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
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 认证token策略
 *
 * @author Telechow
 * @since 2023/4/10 11:27
 */
public interface IAuthenticationTokenStrategy extends InitializingBean {

    /**
     * 根据认证详细信息数据实例化认证token
     *
     * @param details Garoupa的web认证详细信息
     * @return org.springframework.security.authentication.AbstractAuthenticationToken 认证token
     * @author Telechow
     * @since 2023/4/10 11:29
     */
    AbstractAuthenticationToken instanceAuthenticationToken(GaroupaWebAuthenticationDetails details);
}
