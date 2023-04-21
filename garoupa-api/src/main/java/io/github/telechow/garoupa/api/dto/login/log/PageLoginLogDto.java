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
package io.github.telechow.garoupa.api.dto.login.log;

import io.github.telechow.garoupa.api.dto.common.CommonPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 分页查询登录日志dto
 *
 * @author Telechow
 * @since 2023/4/18 17:21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页查询登录日志dto")
public class PageLoginLogDto extends CommonPageDto {

    @Schema(description = "是否登录成功查询条件；false：失败，true：成功")
    private Boolean isSuccess;

    @Schema(description = "用户ip查询条件")
    private String remoteIp;

    @Schema(description = "登录时间起（含）")
    private LocalDateTime createTimeBegin;

    @Schema(description = "登录时间止（不含）")
    private LocalDateTime createTimeEnd;
}
