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
package io.github.telechow.garoupa.web.security.token;

import io.github.telechow.garoupa.api.domain.GaroupaUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * 邮箱地址邮件码认证token
 *
 * @author Telechow
 * @since 2023/3/29 16:21
 */
public class EmailAddressEmailCodeAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 主体
     */
    private final Object principal;

    /**
     * 凭证
     */
    private Object credentials;

    /**
     * 未认证的构造方法，此构造方法返回的token实例永远都是未认证的
     *
     * @param principal   认证主体，此处是用户邮箱地址
     * @param credentials 凭证，此处为邮件验证码
     * @author Telechow
     * @since 2023/3/29 16:53
     */
    public EmailAddressEmailCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    /**
     * 认证通过的构造方法，此构造方法返回的token实例永远是已认证
     *
     * @param principal   认证主体，此处是{@link GaroupaUser}实体
     * @param authorities 权限集合
     * @author Telechow
     * @since 2023/3/29 16:55
     */
    public EmailAddressEmailCodeAuthenticationToken(Object principal
            , Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }

    /**
     * 此工厂方法可以安全的在任何代码中使用并创建一个未认证的邮箱地址邮件验证码认证token
     *
     * @param principal   认证主体，此处为用户邮箱地址
     * @param credentials 凭证，此处为邮件验证码
     * @return io.github.telechow.garoupa.config.security.token.EmailAddressEmailCodeAuthenticationToken 邮箱地址邮件验证码认证token
     * @author Telechow
     * @since 2023/3/29 17:01
     */
    public static EmailAddressEmailCodeAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new EmailAddressEmailCodeAuthenticationToken(principal, credentials);
    }

    /**
     * 此工厂方法可以安全的在任何代码中使用并创建一个通过认证的邮箱地址邮件验证码认证token
     *
     * @param principal   认证主体，此处是{@link GaroupaUser}实体
     * @param authorities 权限集合
     * @return io.github.telechow.garoupa.config.security.token.EmailAddressEmailCodeAuthenticationToken 邮箱地址邮件验证码认证token
     * @author Telechow
     * @since 2023/3/29 17:04
     */
    public static EmailAddressEmailCodeAuthenticationToken authenticated(Object principal
            , Collection<? extends GrantedAuthority> authorities) {
        return new EmailAddressEmailCodeAuthenticationToken(principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
