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
package io.github.telechow.garoupa.web.audit.log.aspect;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson2.JSON;
import io.github.telechow.garoupa.api.entity.AuditLog;
import io.github.telechow.garoupa.api.enums.audit.log.LogTypeEnum;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.audit.log.event.AuditLogEvent;
import io.github.telechow.garoupa.web.utils.AuthenticationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 审计日志切面处理
 *
 * @author Telechow
 * @since 2023/4/20 8:49
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class AuditLogAspect {

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 环绕通知
     *
     * @param point              切入点
     * @param auditLogAnnotation 审计日志注解
     * @return java.lang.Object 方法执行结果
     * @author Telechow
     * @since 2023/4/20 8:58
     */
    @SneakyThrows
    @Around("@annotation(auditLogAnnotation)")
    public Object around(ProceedingJoinPoint point, AuditLogAnnotation auditLogAnnotation) {
        //1.实例化审计日志实体
        AuditLog auditLog = new AuditLog();
        //1.1.审计日志标题、请求用户id
        auditLog.setTitle(auditLogAnnotation.value());
        auditLog.setUserId(AuthenticationUtil.getUserId());
        //1.2.请求用户ip地址、用户代理、请求uri、请求方法
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        auditLog.setRemoteIp(JakartaServletUtil.getClientIP(request))
                .setUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
                .setRequestUri(request.getRequestURI()).setMethod(request.getMethod());
        //1.3.请求参数的json
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        Map<String, Object> requestParamMap = new HashMap<>(parameterNames.length);
        if (ArrayUtil.isNotEmpty(parameterNames)) {
            Object[] args = point.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                requestParamMap.put(parameterNames[i], args[i]);
            }
        }
        auditLog.setRequestParams(JSON.toJSONString(requestParamMap));
        //1.4.日志类型，设置为正常
        auditLog.setLogType(LogTypeEnum.NORMAL.getCode());
        //1.5.请求时间，设置为当前系统时间
        auditLog.setCreateTime(LocalDateTime.now());

        //2.执行切面方法
        Long startTime = System.currentTimeMillis();
        Object obj;
        try {
            obj = point.proceed();
        } catch (Exception e) {
            //2.1.如果执行切面方法抛出异常，则设置审计日志类型为错误，并设置异常信息
            auditLog.setLogType(LogTypeEnum.ERROR.getCode()).setExceptionMessage(e.getMessage());
            //2.2.然后将异常原样抛出
            throw e;
        } finally {
            //3.发送异步日志事件
            Long endTime = System.currentTimeMillis();
            auditLog.setExecuteDuration(endTime - startTime);
            applicationEventPublisher.publishEvent(new AuditLogEvent(auditLog));
        }
        return obj;
    }
}
