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
package io.github.telechow.garoupa.web.service;

import io.github.telechow.garoupa.api.vo.captcha.CaptchaVo;

/**
 * 验证码service接口
 *
 * @author Telechow
 * @since 2023/3/30 16:08
 */
public interface ICaptchaService {

    /**
     * 获取一个图片验证码
     * <li>此接口可以匿名访问</li>
     * <li>验证码的图片将以base64的形式返沪</li>
     *
     * @return io.github.telechow.garoupa.api.vo.captcha.CaptchaVo 验证码vo
     * @author Telechow
     * @since 2023/3/30 13:41
     */
    CaptchaVo get();

    /**
     * 获取一个邮件验证码
     * <li>需要验证是否存在指定邮箱的用户，如果不存在抛出异常</li>
     *
     * @param email 邮箱
     * @author Telechow
     * @since 2023/4/2 20:14
     */
    void getEmailCode(String email);
}
