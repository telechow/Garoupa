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
package io.github.telechow.garoupa.web.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.CaptchaConstant;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.vo.captcha.CaptchaVo;
import io.github.telechow.garoupa.config.email.properties.EmailProperties;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import io.github.telechow.garoupa.web.service.ICaptchaService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 验证码service实现
 *
 * @author Telechow
 * @since 2023/3/30 16:09
 */
@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements ICaptchaService {

    private final IUserAutoService userAutoService;

    private final RedissonClient redissonClient;

    private final JavaMailSender javaMailSender;

    private final EmailProperties emailProperties;

    @Override
    public CaptchaVo get() {
        //1.生成验证码
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(240, 60, 6, 4);
        shearCaptcha.setFont(new Font(null, Font.PLAIN, 40));

        //2.将验证码写入redis中，并设置过期时间
        String captchaKey = IdWorker.get32UUID();
        RBucket<String> captchaBucket = redissonClient.getBucket(
                CaptchaConstant.CAPTCHA_KEY_PREFIX + captchaKey, StringCodec.INSTANCE);
        captchaBucket.set(shearCaptcha.getCode(), 30L, TimeUnit.SECONDS);

        //3.返回结果
        CaptchaVo vo = new CaptchaVo();
        vo.setCaptchaKey(captchaKey).setCaptchaImageBase64(shearCaptcha.getImageBase64Data());
        return vo;
    }

    @Override
    public void getEmailCode(String email) {
        //1.数据验证
        //1.1.验证指定邮箱的用户是否存在，如果不存在抛出异常
        User user = userAutoService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getEmail, email)
                .last("limit 1")
        );
        if (Objects.isNull(user)) {
            return;
        }

        //2.生成邮件验证码，并发送邮件
        final String emailCode = RandomUtil.randomString(6);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailProperties.getUsername());
        message.setTo(email);
        message.setSubject("Garoupa邮件验证码");
        message.setText("你的邮件验证码是：" + emailCode);
        javaMailSender.send(message);

        //3.将邮件验证码写入Redis
        RBucket<String> emailCodeBucket = redissonClient.getBucket(
                CaptchaConstant.EMAIL_CODE_KEY_PREFIX + emailCode, StringCodec.INSTANCE);
        emailCodeBucket.set(emailCode, 1, TimeUnit.MINUTES);
    }
}
