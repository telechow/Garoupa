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
package io.github.telechow.garoupa.web.security.handler;

import com.alibaba.fastjson2.JSON;
import io.github.telechow.garoupa.api.entity.LoginLog;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.web.auto.service.ILoginLogAutoService;
import io.github.telechow.garoupa.web.wrapper.LoginLogWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Garoupa认证失败处理器
 *
 * @author Telechow
 * @since 2023/3/30 21:35
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GaroupaAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginLogWrapper loginLogWrapper;

    private final ILoginLogAutoService loginLogAutoService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException exception) throws IOException {
        //1.设置响应Content-Type和字符集和状态码(认证失败当然给401)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        //2.写入登良路成功日志，如果此逻辑抛出异常，则捕获异常，不要让其影响正常登录
        try {
            LoginLog loginLog = loginLogWrapper.instance(request, null, false);
            loginLogAutoService.save(loginLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        //3.将认证异常包装成错误的响应结果，并写入response
        ResponseResult<Void> responseResult = ResponseResult.fail(ResponseCode.AUTHENTICATION_FAILURE, null
                , Map.of(ResponseResult.META_CAUSE_KEY, exception.getMessage()));
        response.getWriter().write(JSON.toJSONString(responseResult));
    }
}
