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
package io.github.telechow.garoupa.web.wrapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.fastjson2.JSON;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import io.github.telechow.garoupa.api.entity.LoginLog;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.login.log.LoginLogPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import io.github.telechow.garoupa.web.security.details.GaroupaWebAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 登录日志包装器
 *
 * @author Telechow
 * @since 2023/4/18 16:03
 */
@Component
@RequiredArgsConstructor
public class LoginLogWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 HttpServletRequest对象、Garoupa的登录用户信息、是否登录成功 实例化 登录日志实体
     *
     * @param request     HttpServletRequest对象
     * @param garoupaUser Garoupa的登录用户信息
     * @param success     是否登录成功
     * @return io.github.telechow.garoupa.api.entity.LoginLog 登录日志实体
     * @author Telechow
     * @since 2023/4/18 16:06
     */
    public LoginLog instance(HttpServletRequest request, GaroupaUser garoupaUser, boolean success) {
        LoginLog loginLog = new LoginLog();
        //1.用户id、是否登录成功
        loginLog.setIsSuccess(success);
        if (Objects.isNull(garoupaUser)) {
            loginLog.setUserId(null);
        } else {
            loginLog.setUserId(garoupaUser.getUser().getId());
        }

        //2.登录方式
        String loginMode = request.getHeader(GaroupaWebAuthenticationDetails.LOGIN_MODE_HEADER);
        loginLog.setLoginMode(loginMode);

        //3.3.用户ip、用户代理
        String clientIP = JakartaServletUtil.getClientIP(request);
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        loginLog.setRemoteIp(clientIP).setUserAgent(userAgent);

        //3.4.登录参数
        loginLog.setLoginParam(JSON.toJSONString(request.getParameterMap()));
        return loginLog;
    }

    /**
     * 将 登录日志实体集合 包装成 登录日志分页vo列表
     *
     * @param loginLogCollection 登录日志实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.login.log.LoginLogPageVo> 登录日志分页vo列表
     * @author Telechow
     * @since 2023/4/18 17:59
     */
    public List<LoginLogPageVo> loginLogCollectionToLoginLogPageVoList(Collection<LoginLog> loginLogCollection) {
        //1.如果登录日志实体集合为空，则返回空列表
        if (CollectionUtil.isEmpty(loginLogCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        //2.1.是否系统字典
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper
                .listSystemDictItemByDictCode(SystemDictEnum.TRUE_FALSE.getDictCode());

        //3.包装数据
        List<LoginLogPageVo> loginLogPageVos = new ArrayList<>(loginLogCollection.size());
        for (LoginLog loginLog : loginLogCollection) {
            LoginLogPageVo vo = new LoginLogPageVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(loginLog.getId()).setUserId(loginLog.getUserId()).setLoginMode(loginLog.getLoginMode())
                    .setIsSuccess(loginLog.getIsSuccess()).setRemoteIp(loginLog.getRemoteIp())
                    .setUserAgent(loginLog.getUserAgent()).setLoginParam(loginLog.getLoginParam())
                    .setCreateTime(loginLog.getCreateTime());
            //3.2.包装是否登录成功名称
            Optional.ofNullable(vo.getIsSuccess())
                    .flatMap(is -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), is.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setIsSuccessName(sdi.getItemText()));
            loginLogPageVos.add(vo);
        }
        return loginLogPageVos;
    }
}
