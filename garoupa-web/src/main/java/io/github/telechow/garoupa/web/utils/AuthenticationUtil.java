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
package io.github.telechow.garoupa.web.utils;

import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 认证工具类
 *
 * @author Telechow
 * @since 2023/4/6 16:26
 */
public class AuthenticationUtil {

    /**
     * 获取当前登录用户实体，如果获取失败返回null
     *
     * @return io.github.telechow.garoupa.api.entity.User 用户实体
     * @author Telechow
     * @since 2023/4/6 16:27
     */
    public static User getUser() {
        //1.获取当前登录用户的认证对象，如果为null，则没有登录，返回null
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }

        //2.获取用户实体
        if (authentication.getPrincipal() instanceof GaroupaUser garoupaUser) {
            return garoupaUser.getUser();
        }

        return null;
    }

    /**
     * 获取当前登录用户id，如果获取失败，返回-1
     *
     * @return java.lang.Long 当前登录用户id
     * @author Telechow
     * @since 2023/4/6 16:30
     */
    public static Long getUserId() {
        User user = getUser();
        if (Objects.isNull(user)) {
            return -1L;
        }
        return user.getId();
    }
}
