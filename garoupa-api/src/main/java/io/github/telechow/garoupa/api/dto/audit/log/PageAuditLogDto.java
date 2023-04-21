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
package io.github.telechow.garoupa.api.dto.audit.log;

import io.github.telechow.garoupa.api.dto.common.CommonPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分页查询审计日志dto
 *
 * @author Telechow
 * @since 2023/4/20 10:08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页查询审计日志dto")
public class PageAuditLogDto extends CommonPageDto {

    @Schema(description = "审计日志标题查询条件；限250字")
    @Size(max = 250,message = "审计日志标题查询条件太长")
    private String title;

    @Schema(description = "请求用户id查询条件")
    private Long userId;

    @Schema(description = "请求用户ip地址，非空")
    private String remoteIp;

    @Schema(description = "日志类型查询条件；0：正常；10：错误")
    private Integer logType;

    @Schema(description = "请求时间起（含）")
    private LocalDateTime createTimeBegin;

    @Schema(description = "请求时间止（不含）")
    private LocalDateTime createTimeEnd;
}
