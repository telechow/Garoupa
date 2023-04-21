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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.login.log.PageLoginLogDto;
import io.github.telechow.garoupa.api.entity.LoginLog;
import io.github.telechow.garoupa.api.vo.login.log.LoginLogPageVo;
import io.github.telechow.garoupa.web.auto.service.ILoginLogAutoService;
import io.github.telechow.garoupa.web.service.ILoginLogService;
import io.github.telechow.garoupa.web.wrapper.LoginLogWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 登录日志service实现
 *
 * @author Telechow
 * @since 2023/4/18 17:03
 */
@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl implements ILoginLogService {

    private final LoginLogWrapper loginLogWrapper;

    private final ILoginLogAutoService loginLogAutoService;

    @Override
    public Page<LoginLogPageVo> page(PageLoginLogDto dto) {
        //1.查询数据
        Page<LoginLog> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<LoginLog> lambdaQueryWrapper = Wrappers.<LoginLog>lambdaQuery()
                //1.1.按照创建时间降序排序
                .orderByDesc(LoginLog::getCreateTime);
        //1.2.设置是否登录成功查询条件
        Optional.ofNullable(dto.getIsSuccess())
                .ifPresent(s -> lambdaQueryWrapper.eq(LoginLog::getIsSuccess, s));
        //1.3.设置用户ip查询条件
        if (StrUtil.isNotBlank(dto.getRemoteIp())) {
            lambdaQueryWrapper.apply("match(remote_ip) against({0} in boolean mode)", "+" + dto.getRemoteIp());
        }
        //1.4.设置登录时间查询条件
        Optional.ofNullable(dto.getCreateTimeBegin())
                .ifPresent(cte -> lambdaQueryWrapper.ge(LoginLog::getCreateTime, dto.getCreateTimeBegin()));
        Optional.ofNullable(dto.getCreateTimeEnd())
                .ifPresent(ctb -> lambdaQueryWrapper.lt(LoginLog::getCreateTime, dto.getCreateTimeEnd()));
        page = loginLogAutoService.page(page, lambdaQueryWrapper);

        //2.包装数据
        List<LoginLogPageVo> loginLogPageVos = loginLogWrapper
                .loginLogCollectionToLoginLogPageVoList(page.getRecords());
        Page<LoginLogPageVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(loginLogPageVos);
        return result;
    }
}
