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
package io.github.telechow.garoupa.api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.ahoo.cosid.annotation.CosId;

/**
 * <p>
 * 资源（菜单或按钮）表
 * </p>
 *
 * @author telechow
 * @since 2023-03-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("garoupa_resource")
@Schema(name = "Resource", description = "资源（菜单或按钮）表")
public class Resource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单按钮id")
    @CosId(value = "Resource")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Schema(description = "上级id，非空；如果没有上级则为0；上级资源只能是菜单")
    private Long parentId;

    @Schema(description = "资源类型，非空；0：菜单，10：按钮")
    private Integer resourceType;

    @Schema(description = "资源编码，非空；限200字；形如“模块名:菜单名”；唯一")
    private String resourceCode;

    @Schema(description = "资源名称，非空；限200字")
    private String resourceName;

    @Schema(description = "图标；限250字")
    private String icon;

    @Schema(description = "路由；限250字")
    private String route;

    @Schema(description = "排序，非空；小号在前")
    private Integer sortNumber;

    @Schema(description = "创建人id，非空")
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @Schema(description = "创建时间，非空")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "最后修改人id，非空")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @Schema(description = "最后修改时间，非空")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除标识，非空；0：未删除，其他：已删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Long deletedTag;
}
