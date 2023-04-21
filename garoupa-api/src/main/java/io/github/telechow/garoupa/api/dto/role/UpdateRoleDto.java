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
package io.github.telechow.garoupa.api.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 修改角色dto
 *
 * @author Telechow
 * @since 2023/4/15 20:15
 */
@Data
@Accessors(chain = true)
@Schema(description = "修改角色dto")
public class UpdateRoleDto implements Serializable {

    @Schema(description = "角色编码，非空；限50字，小写下划线命名法；唯一")
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50,message = "角色编码太长")
    private String roleCode;

    @Schema(description = "角色名称，非空；限50字；唯一")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50,message = "角色名称太长")
    private String roleName;
}
