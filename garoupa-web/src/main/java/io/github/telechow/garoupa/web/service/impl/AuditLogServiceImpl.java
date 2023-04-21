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
import io.github.telechow.garoupa.api.dto.audit.log.PageAuditLogDto;
import io.github.telechow.garoupa.api.entity.AuditLog;
import io.github.telechow.garoupa.api.vo.audit.log.AuditLogPageVo;
import io.github.telechow.garoupa.web.auto.service.IAuditLogAutoService;
import io.github.telechow.garoupa.web.service.IAuditLogService;
import io.github.telechow.garoupa.web.wrapper.AuditLogWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 审计日志实现
 *
 * @author Telechow
 * @since 2023/4/20 9:59
 */
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements IAuditLogService {

    private final AuditLogWrapper auditLogWrapper;

    private final IAuditLogAutoService auditLogAutoService;

    @Override
    public Page<AuditLogPageVo> page(PageAuditLogDto dto) {
        //1.查询数据
        Page<AuditLog> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<AuditLog> lambdaQueryWrapper = Wrappers.<AuditLog>lambdaQuery()
                //1.1.按照创建时间降序排序
                .orderByDesc(AuditLog::getCreateTime);
        //1.2.设置审计日志标题查询条件
        if (StrUtil.isNotBlank(dto.getTitle())) {
            lambdaQueryWrapper.eq(AuditLog::getTitle, dto.getTitle());
        }
        //1.3.设置请求用户id查询条件
        Optional.ofNullable(dto.getUserId())
                .ifPresent(uid -> lambdaQueryWrapper.eq(AuditLog::getUserId, uid));
        //1.4.设置用户ip查询条件
        if (StrUtil.isNotBlank(dto.getRemoteIp())) {
            lambdaQueryWrapper.apply("match(remote_ip) against({0} in boolean mode)", "+" + dto.getRemoteIp());
        }
        //1.5.设置日志类型查询条件
        Optional.ofNullable(dto.getLogType())
                .ifPresent(lt -> lambdaQueryWrapper.eq(AuditLog::getLogType, lt));
        //1.6.设置请求时间查询条件
        Optional.ofNullable(dto.getCreateTimeBegin())
                .ifPresent(cte -> lambdaQueryWrapper.ge(AuditLog::getCreateTime, dto.getCreateTimeBegin()));
        Optional.ofNullable(dto.getCreateTimeEnd())
                .ifPresent(ctb -> lambdaQueryWrapper.lt(AuditLog::getCreateTime, dto.getCreateTimeEnd()));
        page = auditLogAutoService.page(page, lambdaQueryWrapper);

        //2.包装数据
        List<AuditLogPageVo> auditLogPageVos = auditLogWrapper
                .auditLogCollectionToAuditLogPageVoList(page.getRecords());
        Page<AuditLogPageVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(auditLogPageVos);
        return result;
    }
}
