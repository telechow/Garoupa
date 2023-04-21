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
