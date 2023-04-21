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
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.LinkedHashSet;

/**
 * 角色关联权限dto
 *
 * @author Telechow
 * @since 2023/4/15 23:16
 */
@Data
@Accessors(chain = true)
@Schema(description = "角色关联权限dto")
public class RoleAssociatePermissionDto implements Serializable {

    @Schema(description = "权限id列表，可为空")
    private LinkedHashSet<@Valid @NotNull(message = "权限id不能为空") Long> permissionIds;
}
