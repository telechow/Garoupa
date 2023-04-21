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
package io.github.telechow.garoupa.api.dto.system.dict;

import io.github.telechow.garoupa.api.dto.common.CommonPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询系统字典dto
 *
 * @author Telechow
 * @since 2023/4/11 16:18
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页查询系统字典dto")
public class PageSystemDictDto extends CommonPageDto {

    @Schema(description = "字典编码模糊查询条件，非空；限250字，小写下划线命名法")
    private String dictCodeLike;

    @Schema(description = "字典名称模糊查询条件，非空；限250字")
    private String dictNameLike;
}
