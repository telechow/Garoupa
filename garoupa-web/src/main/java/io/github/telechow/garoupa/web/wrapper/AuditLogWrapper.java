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
import io.github.telechow.garoupa.api.entity.AuditLog;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.audit.log.AuditLogPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 审计日志包装器
 *
 * @author Telechow
 * @since 2023/4/20 10:18
 */
@Component
@RequiredArgsConstructor
public class AuditLogWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 将 审计日志实体集合 包装成 审计日志分页vo列表
     *
     * @param auditLogCollection 审计日志实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.audit.log.AuditLogPageVo> 审计日志分页vo列表
     * @author Telechow
     * @since 2023/4/20 10:24
     */
    public List<AuditLogPageVo> auditLogCollectionToAuditLogPageVoList(Collection<AuditLog> auditLogCollection) {
        //1.如果审计日志实体集合为空，则返回空列表
        if (CollectionUtil.isEmpty(auditLogCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        //2.1.审计日志日志类型系统字典
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper
                .listSystemDictItemByDictCode(SystemDictEnum.AUDIT_LOG_LOG_TYPE.getDictCode());

        //3.包装数据
        List<AuditLogPageVo> auditLogPageVos = new ArrayList<>(auditLogCollection.size());
        for (AuditLog auditLog : auditLogCollection) {
            AuditLogPageVo vo = new AuditLogPageVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(auditLog.getId()).setTitle(auditLog.getTitle()).setUserId(auditLog.getUserId())
                    .setRemoteIp(auditLog.getRemoteIp()).setUserAgent(auditLog.getUserAgent())
                    .setRequestUri(auditLog.getRequestUri()).setMethod(auditLog.getMethod())
                    .setRequestParams(auditLog.getRequestParams()).setExecuteDuration(auditLog.getExecuteDuration())
                    .setLogType(auditLog.getLogType()).setExceptionMessage(auditLog.getExceptionMessage())
                    .setCreateTime(auditLog.getCreateTime());
            //3.2.包装日志类型名称
            Optional.ofNullable(vo.getLogType())
                    .flatMap(lt -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), lt.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setLogTypeName(sdi.getItemText()));
            auditLogPageVos.add(vo);
        }
        return auditLogPageVos;
    }
}
