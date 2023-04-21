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
package io.github.telechow.garoupa.config.exception.handler;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.exception.LockException;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.config.exception.ServiceException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author Telechow
 * @since 2023/4/6 21:32
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Lock4j分布式锁异常处理器
     *
     * @param e        LockException
     * @param response http响应对象
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/6 21:34
     */
    @ExceptionHandler(LockException.class)
    public ResponseResult<Void> lockException(LockException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        return ResponseResult.fail(ResponseCode.ON_LOCK_FAIL, e.getMessage());
    }

    /**
     * 违反数据库唯一约束条件异常处理器
     *
     * @param e        DuplicateKeyException
     * @param response http响应对象
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/6 22:20
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseResult<Void> duplicateKeyException(DuplicateKeyException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseResult.fail(ResponseCode.VIOLATION_SQL_INTEGRITY_CONSTRAINT);
    }

    /**
     * 请求参数校验异常处理器
     *
     * @param e        MethodArgumentNotValidException
     * @param response http响应对象
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/6 23:25
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException e
            , HttpServletResponse response) {
        log.error(e.getMessage(), e);
        Map<String, Object> meta = new HashMap<>(e.getBindingResult().getFieldErrorCount());
        for (int i = 0; i < e.getBindingResult().getFieldErrorCount(); i++) {
            meta.put(String.valueOf(i), e.getBindingResult().getFieldErrors().get(i).getField() + StrPool.DASHED +
                    e.getBindingResult().getFieldErrors().get(i).getDefaultMessage());
        }
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseResult.fail(ResponseCode.REQUEST_PARAM_VALIDATE_FAIL, null, meta);
    }

    /**
     * 业务异常处理器
     *
     * @param e        ServiceException
     * @param response http响应对象
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 15:03
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseResult<Void> serviceException(ServiceException e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        //B类错误为客户端错误，返回400，A类错误为本系统错误，C类错误为三方系统错误都返回500
        switch (e.getResponseCode().getCode().substring(0, 1)) {
            case "B":
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            case "A":
            case "C":
            default:
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                break;
        }
        return ResponseResult.fail(e.getResponseCode(), e.getMessage());
    }

    /**
     * 通用异常处理器
     *
     * @param e        异常
     * @param response http响应对象
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/6 21:59
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> exception(Exception e, HttpServletResponse response) {
        Throwable t = e;
        while (StrUtil.isBlank(t.getMessage())) {
            t = t.getCause();
        }
        log.error(t.getMessage(), t);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseResult.fail(ResponseCode.FAIL, t.getMessage());
    }
}
