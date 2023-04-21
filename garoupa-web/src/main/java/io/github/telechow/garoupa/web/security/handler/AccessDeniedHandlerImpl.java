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
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 授权拒绝处理器
 *
 * @author Telechow
 * @since 2023/4/3 23:23
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException accessDeniedException) throws IOException {
        //1.设置响应Content-Type和字符集和状态码(授权失败当然给403)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());

        //2.将认证异常包装成错误的响应结果，并写入response
        ResponseResult<Void> responseResult = ResponseResult.fail(ResponseCode.ACCESS_DENIED, null
                , Map.of(ResponseResult.META_CAUSE_KEY, accessDeniedException.getMessage()));
        response.getWriter().write(JSON.toJSONString(responseResult));
    }
}
