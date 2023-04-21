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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改系统参数dto
 *
 * @author Telechow
 * @since 2023/4/7 21:30
 */
@Data
@Accessors(chain = true)
@Schema(description = "修改系统参数dto")
public class UpdateSystemParamDto implements Serializable {

    @Schema(description = "参数编码，非空，唯一；小驼峰命名法，限250字")
    @NotBlank(message = "参数编码不能为空")
    @Size(max = 250, message = "参数编码太长")
    private String paramCode;

    @Schema(description = "参数名称，非空，唯一；限250字")
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 250, message = "参数名称太长")
    private String paramName;

    @Schema(description = "参数值，非空；限2000字")
    @NotBlank(message = "参数值不能为空")
    private String paramValue;
}
