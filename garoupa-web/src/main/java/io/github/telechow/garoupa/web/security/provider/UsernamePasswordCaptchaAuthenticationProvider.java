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
import io.github.telechow.garoupa.web.security.service.UserNamePasswordUserDetailService;
import io.github.telechow.garoupa.web.security.token.UsernamePasswordCaptchaAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 用户名密码验证码认证提供者
 *
 * @author Telechow
 * @since 2023/3/29 17:22
 */
@Component
@RequiredArgsConstructor
public class UsernamePasswordCaptchaAuthenticationProvider implements AuthenticationProvider {

    private final RedissonClient redissonClient;

    private final UserNamePasswordUserDetailService userNamePasswordUserDetailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> authentication) {
        //只支持UsernamePasswordAuthenticationToken类型的token认证
        return UsernamePasswordCaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordCaptchaAuthenticationToken authenticationToken = (UsernamePasswordCaptchaAuthenticationToken) authentication;
        //1.验证验证码，如果校验失败抛出异常
        //1.1.查询redis，得到验证码code
        RBucket<String> captchaBucket = redissonClient.getBucket(
                CaptchaConstant.CAPTCHA_KEY_PREFIX + authenticationToken.getCaptchaKey(), StringCodec.INSTANCE);
        String captchaCode = captchaBucket.get();
        //1.2.如果查询不到验证码，则用户输入的验证码不存在或已过期，抛出异常
        if (StrUtil.isBlank(captchaCode)) {
            throw new BadCredentialsException(ResponseCode.AUTHENTICATION_CAPTCHA_NOT_EXIST_OR_EXPIRE.getMsg());
        }
        //1.3.匹配验证码，忽略大小写
        if (!StrUtil.equalsIgnoreCase(captchaCode, authenticationToken.getCaptchaCode())) {
            throw new BadCredentialsException(ResponseCode.AUTHENTICATION_CAPTCHA_NOT_MATCH.getMsg());
        }
        //1.4.匹配成功，删除已经匹配的验证码
        captchaBucket.delete();

        //2.使用指定UserDetailsService的实现类查询UserDetails（我们使用GaroupaUser），查询不到抛出异常
        UserDetails userDetails = userNamePasswordUserDetailService
                .loadUserByUsername(authenticationToken.getPrincipal().toString());
        GaroupaUser garoupaUser = (GaroupaUser) userDetails;

        //3.验证密码
        if (StrUtil.isBlank(authenticationToken.getCredentials().toString())) {
            throw new BadCredentialsException(ResponseCode.USERNAME_PASSWORD_CAPTCHA_AUTHENTICATION_NOT_INPUT_PASSWORD.getMsg());
        }
        final boolean notMatches = !passwordEncoder.matches(
                authenticationToken.getCredentials().toString(), userDetails.getPassword());
        if (notMatches) {
            throw new BadCredentialsException(ResponseCode.USERNAME_PASSWORD_CAPTCHA_AUTHENTICATION_PASSWORD_NOT_MATCHES.getMsg());
        }

        //4.构造认证成功的UsernamePasswordCaptchaAuthenticationToken并返回
        return UsernamePasswordCaptchaAuthenticationToken.authenticated(
                garoupaUser, authenticationToken.getCredentials(), garoupaUser.getAuthorities());
    }
}
