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
package io.github.telechow.garoupa.web.controller.management;

import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.captcha.CaptchaVo;
import io.github.telechow.garoupa.web.service.ICaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码-控制器
 *
 * @author Telechow
 * @since 2023/3/30 13:40
 */
@RestController
@RequestMapping("/m/captcha")
@Tag(name = "验证码-控制器", description = "验证码-控制器")
@RequiredArgsConstructor
public class CaptchaController {

    private final ICaptchaService captchaService;

    /**
     * 获取一个图片验证码
     * <li>此接口可以匿名访问</li>
     * <li>验证码的图片将以base64的形式返沪</li>
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<io.github.telechow.garoupa.api.vo.captcha.CaptchaVo> 验证码vo
     * @author Telechow
     * @since 2023/3/30 13:41
     */
    @GetMapping("/get")
    @Operation(summary = "获取一个图片验证码"
            , description = "<li>此接口可以匿名访问</li>" +
            "<li>验证码的图片将以base64的形式返回</li>")
    public ResponseResult<CaptchaVo> get() {
        return ResponseResult.data(captchaService.get());
    }

    /**
     * 获取一个邮件验证码
     * <li>需要验证是否存在指定邮箱的用户，如果不存在则什么都不做</li>
     *
     * @param email 邮箱
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/2 20:14
     */
    @GetMapping("/get/email/code")
    @Operation(summary = "获取一个邮件验证码"
            , description = "<li>需要验证是否存在指定邮箱的用户，如果不存在则什么都不做</li>")
    public ResponseResult<Void> getEmailCode(@RequestParam String email) {
        captchaService.getEmailCode(email);
        return ResponseResult.ok();
    }
}
