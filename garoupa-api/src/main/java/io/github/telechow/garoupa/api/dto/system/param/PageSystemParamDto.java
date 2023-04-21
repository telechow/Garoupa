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
package io.github.telechow.garoupa.api.dto.system.param;

import io.github.telechow.garoupa.api.dto.common.CommonPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询系统参数dto
 *
 * @author Telechow
 * @since 2023/4/7 22:47
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页查询系统参数dto")
public class PageSystemParamDto extends CommonPageDto {

    @Schema(description = "系统参数分类id查询条件，非空")
    @NotNull(message = "系统参数分类id不能为空")
    private Long paramCategoryId;

    @Schema(description = "系统参数编码模糊查询条件")
    @Size(max = 250, message = "参数编码太长")
    private String paramCodeLike;

    @Schema(description = "系统参数名称模糊查询条件")
    @Size(max = 250, message = "参数名称太长")
    private String paramNameLike;
}
