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
package io.github.telechow.garoupa.api.vo.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 权限分页vo
 *
 * @author Telechow
 * @since 2023/4/14 15:59
 */
@Data
@Accessors(chain = true)
@Schema(description = "权限分页vo")
public class PermissionPageVo implements Serializable {

    @Schema(description = "权限id")
    private Long id;

    @Schema(description = "权限编码，非空；限200字；形如“模块名:实体名:功能名”；唯一")
    private String permissionCode;

    @Schema(description = "权限名称，非空；限200字")
    private String permissionName;
}
