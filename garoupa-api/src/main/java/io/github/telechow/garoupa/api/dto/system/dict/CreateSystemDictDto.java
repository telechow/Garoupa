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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 创建系统字典dto
 *
 * @author Telechow
 * @since 2023/4/10 21:21
 */
@Data
@Accessors(chain = true)
@Schema(description = "创建系统字典dto")
public class CreateSystemDictDto implements Serializable {

    @Schema(description = "字典编码，非空；限250字，小写下划线命名法")
    @NotBlank(message = "字典编码不能为空")
    @Size(max = 250,message = "字典编码太长")
    private String dictCode;

    @Schema(description = "字典名称，非空；限250字")
    @NotBlank(message = "字典名称不能为空")
    @Size(max = 250,message = "字典名称太长")
    private String dictName;

    @Schema(description = "字典描述；限1000字")
    @Size(max = 1000,message = "字典描述太长")
    private String dictDescribe;

    @Schema(description = "排序，非空；小号在前")
    @NotNull(message = "排序不能为空")
    private Integer sortNumber;
}
