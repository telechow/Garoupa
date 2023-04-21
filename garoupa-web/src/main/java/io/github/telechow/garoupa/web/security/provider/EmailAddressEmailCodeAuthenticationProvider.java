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
package io.github.telechow.garoupa.web.security.provider;

import cn.hutool.core.util.StrUtil;
import io.github.telechow.garoupa.api.constant.CaptchaConstant;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import io.github.telechow.garoupa.web.security.service.EmailAddressUserDetailService;
import io.github.telechow.garoupa.web.security.token.EmailAddressEmailCodeAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 邮箱地址邮件验证码认证提供者
 *
 * @author Telechow
 * @since 2023/4/1 16:47
 */
@Component
@RequiredArgsConstructor
public class EmailAddressEmailCodeAuthenticationProvider implements AuthenticationProvider {

    private final RedissonClient redissonClient;

    private final EmailAddressUserDetailService emailAddressUserDetailService;

    @Override
    public boolean supports(Class<?> authentication) {
        //只支持EmailAddressEmailCodeAuthenticationToken类型的token认证
        return EmailAddressEmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAddressEmailCodeAuthenticationToken authenticationToken = (EmailAddressEmailCodeAuthenticationToken) authentication;
        //1.使用指定UserDetailsService的实现类查询UserDetails（我们使用GaroupaUser），查询不到抛出异常
        UserDetails userDetails = emailAddressUserDetailService
                .loadUserByUsername(authenticationToken.getPrincipal().toString());
        GaroupaUser garoupaUser = (GaroupaUser) userDetails;

        //2.验证邮件验证码
        //2.1.查询redis，得到邮件验证码
        RBucket<String> emailCodeBucket = redissonClient.getBucket(
                CaptchaConstant.EMAIL_CODE_KEY_PREFIX + authenticationToken.getCredentials().toString(), StringCodec.INSTANCE);
        String emailCode = emailCodeBucket.get();
        //2.2.如果查询不到邮件验证码，则用户输入的邮件验证码不存在或已过期，抛出异常
        if (StrUtil.isBlank(emailCode)) {
            throw new BadCredentialsException(ResponseCode.AUTHENTICATION_EMAIL_CODE_NOT_MATCH_OR_EXPIRE.getMsg());
        }
        //2.3.匹配邮件验证码，忽略大小写
        if (!StrUtil.equalsIgnoreCase(emailCode, authenticationToken.getCredentials().toString())) {
            throw new BadCredentialsException(ResponseCode.AUTHENTICATION_EMAIL_CODE_NOT_MATCH_OR_EXPIRE.getMsg());
        }
        //2.4.匹配成功，删除已经匹配的邮件验证码
        emailCodeBucket.delete();

        //3.构造认证成功的EmailAddressEmailCodeAuthenticationToken并返回
        return EmailAddressEmailCodeAuthenticationToken.authenticated(garoupaUser, garoupaUser.getAuthorities());
    }

}
