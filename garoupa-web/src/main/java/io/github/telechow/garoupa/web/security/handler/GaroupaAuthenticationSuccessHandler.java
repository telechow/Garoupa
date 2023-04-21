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

import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson2.JSON;
import io.github.telechow.garoupa.api.entity.LoginLog;
import io.github.telechow.garoupa.api.enums.system.param.SystemParamEnum;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.login.LoginVo;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import io.github.telechow.garoupa.web.auto.service.ILoginLogAutoService;
import io.github.telechow.garoupa.web.helper.SystemParamHelper;
import io.github.telechow.garoupa.web.security.properties.JwtProperties;
import io.github.telechow.garoupa.web.wrapper.LoginLogWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * Garoupa认证成功处理器
 *
 * @author Telechow
 * @since 2023/3/30 12:52
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GaroupaAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginLogWrapper loginLogWrapper;

    private final ILoginLogAutoService loginLogAutoService;

    private final SystemParamHelper systemParamHelper;

    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {
        //1.设置响应Content-Type和字符集和http状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(HttpStatus.OK.value());

        //2.生成jwt，并设置在响应头Authorization中
        GaroupaUser garoupaUser = (GaroupaUser) authentication.getPrincipal();
        //2.1.获取jwt过期时间
        Long expireSecond = systemParamHelper.getLongValue(SystemParamEnum.EXPIRE_SECOND);
        Map<String, Object> payloadMap = Map.of(JWTPayload.EXPIRES_AT
                , LocalDateTime.now().plusSeconds(expireSecond).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                , JWTPayload.SUBJECT, garoupaUser.getUser().getId());
        String jwt = JWTUtil.createToken(payloadMap, jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
        response.setHeader(HttpHeaders.AUTHORIZATION, jwt);

        //3.写入登良路成功日志，如果此逻辑抛出异常，则捕获异常，不要让其影响正常登录
        try {
            LoginLog loginLog = loginLogWrapper.instance(request, garoupaUser, true);
            loginLogAutoService.save(loginLog);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        //4.将用户数据设置在响应体中
        LoginVo vo = new LoginVo();
        vo.setId(garoupaUser.getUser().getId()).setUserName(garoupaUser.getUser().getUserName())
                .setNickName(garoupaUser.getUser().getNickName()).setAvatarUri(garoupaUser.getUser().getAvatarUri())
                .setCreateTime(garoupaUser.getUser().getCreateTime()).setResourceCodeList(garoupaUser.getResources());
        ResponseResult<LoginVo> responseResult = ResponseResult.data(vo);
        response.getWriter().write(JSON.toJSONString(responseResult));
    }
}
