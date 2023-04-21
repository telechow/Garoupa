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
package io.github.telechow.garoupa.web.security.details;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Garoupa的web认证详细信息
 *
 * @author Telechow
 * @since 2023/3/30 9:45
 */
@Getter
@ToString
public class GaroupaWebAuthenticationDetails extends WebAuthenticationDetails {

    //登录方式
    private final String loginMode;

    //用户名密码相关
    private final String username;
    private final String password;

    //验证码相关
    private final String captchaKey;
    private final String captchaCode;

    //邮箱相关
    private final String emailAddress;
    private final String emailCode;

    public static final String LOGIN_MODE_HEADER = "Login-Mode";

    public static final String USERNAME_PARAMETER = "username";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String CAPTCHA_KEY_HEADER = "Captcha-Key";
    public static final String CAPTCHA_CODE_HEADER = "Captcha-Code";

    public static final String EMAIL_ADDRESS_PARAMETER = "emailAddress";
    public static final String EMAIL_CODE_PARAMETER = "emailCode";

    /**
     * 构造方法
     * <li>将会把所有的认证请求详细信息解析到此类中</li>
     *
     * @param request http请求对象
     * @author Telechow
     * @since 2023/3/30 9:51
     */
    public GaroupaWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        String loginModeHeader = request.getHeader(LOGIN_MODE_HEADER);
        this.loginMode = StrUtil.isBlank(loginModeHeader) ? "" : loginModeHeader;

        this.username = request.getParameter(USERNAME_PARAMETER);
        this.password = request.getParameter(PASSWORD_PARAMETER);

        this.captchaKey = request.getHeader(CAPTCHA_KEY_HEADER);
        this.captchaCode = request.getHeader(CAPTCHA_CODE_HEADER);

        this.emailAddress = request.getParameter(EMAIL_ADDRESS_PARAMETER);
        this.emailCode = request.getParameter(EMAIL_CODE_PARAMETER);
    }
}
