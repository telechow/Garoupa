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
package io.github.telechow.garoupa.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.audit.log.PageAuditLogDto;
import io.github.telechow.garoupa.api.vo.audit.log.AuditLogPageVo;

/**
 * 审计日志service接口
 *
 * @author Telechow
 * @since 2023/4/20 9:59
 */
public interface IAuditLogService {

    /**
     * 分页查询审计日志
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询审计日志dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.audit.log.AuditLogPageVo> 分页的审计日志分页vo
     * @author Telechow
     * @since 2023/4/20 10:13
     */
    Page<AuditLogPageVo> page(PageAuditLogDto dto);
}
