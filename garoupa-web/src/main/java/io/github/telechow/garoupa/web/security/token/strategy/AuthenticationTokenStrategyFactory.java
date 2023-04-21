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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证token策略工厂
 *
 * @author Telechow
 * @since 2023/4/10 11:31
 */
public class AuthenticationTokenStrategyFactory {

    public static final Map<String, IAuthenticationTokenStrategy> STRATEGY_MAP = new ConcurrentHashMap<>(8);

    /**
     * 将认证token策略注册到策略工厂
     *
     * @param loginMode 登录方式字符串
     * @param strategy  认证token策略接口
     * @author Telechow
     * @since 2023/4/10 13:13
     */
    public static void register(String loginMode, IAuthenticationTokenStrategy strategy) {
        STRATEGY_MAP.put(loginMode, strategy);
    }

    /**
     * 根据登录方式字符串获取认证token策略接口
     *
     * @param loginMode 登录方式字符串
     * @return io.github.telechow.garoupa.web.security.token.strategy.IAuthenticationTokenStrategy 认证token策略接口
     * @author Telechow
     * @since 2023/4/10 13:24
     */
    public static IAuthenticationTokenStrategy getStrategy(String loginMode) {
        return STRATEGY_MAP.get(loginMode);
    }
}
