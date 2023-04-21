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
package io.github.telechow.garoupa.api.dto.system.dict.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改系统字典项dto
 *
 * @author Telechow
 * @since 2023/4/11 23:32
 */
@Data
@Accessors(chain = true)
@Schema(description = "修改系统字典项dto")
public class UpdateSystemDictItemDto implements Serializable {

    @Schema(description = "字典项值，非空；限50字")
    @NotBlank(message = "字典项值不能为空")
    @Size(max = 50,message = "字典项值太长")
    private String itemValue;

    @Schema(description = "字典项文本，非空；限50字")
    @NotBlank(message = "字典项文本不能为空")
    @Size(max = 50,message = "字典项文本太长")
    private String itemText;

    @Schema(description = "排序，非空；小号在前")
    @NotNull(message = "排序不能为空")
    private Integer sortNumber;
}
