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
package io.github.telechow.garoupa.api.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 创建权限dto
 *
 * @author Telechow
 * @since 2023/4/14 15:20
 */
@Data
@Accessors(chain = true)
@Schema(description = "创建权限dto")
public class CreatePermissionDto implements Serializable {

    @Schema(description = "权限编码，非空；限200字；形如“模块名:实体名:功能名”；唯一")
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 200, message = "权限编码太长")
    private String permissionCode;

    @Schema(description = "权限名称，非空；限200字")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 200, message = "权限名称太长")
    private String permissionName;

    @Schema(description = "菜单资源id，非空")
    @NotNull(message = "菜单id不能为空")
    private Long resourceId;
}
