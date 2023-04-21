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
package io.github.telechow.garoupa.api.dto.resource;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 创建资源dto
 *
 * @author Telechow
 * @since 2023/4/13 15:59
 */
@Data
@Accessors(chain = true)
@Schema(description = "创建资源dto")
public class CreateResourceDto implements Serializable {

    @Schema(description = "上级id，非空；如果没有上级则为0；上级资源只能是菜单")
    @NotNull(message = "上级id不能为空")
    private Long parentId;

    @Schema(description = "资源类型，非空；0：菜单，10：按钮")
    @NotNull(message = "资源类型不能为空")
    private Integer resourceType;

    @Schema(description = "资源编码，非空；限200字；形如“模块名:菜单名”；唯一")
    @NotBlank(message = "资源编码不能为空")
    @Size(max = 200, message = "资源编码太长")
    private String resourceCode;

    @Schema(description = "资源名称，非空；限200字")
    @NotBlank(message = "资源名称不能为空")
    @Size(max = 200, message = "资源名称太长")
    private String resourceName;

    @Schema(description = "图标；限250字")
    @Size(max = 250, message = "图标太长")
    private String icon;

    @Schema(description = "路由；限250字")
    @Size(max = 250, message = "路由太长")
    private String route;

    @Schema(description = "排序，非空；小号在前")
    @NotNull(message = "排序不能为空")
    private Integer sortNumber;
}
