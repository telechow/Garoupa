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
 * 权限表
 * </p>
 *
 * @author telechow
 * @since 2023-03-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("garoupa_permission")
@Schema(name = "Permission", description = "权限表")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "权限id")
    @CosId(value = "Permission")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Schema(description = "权限编码，非空；限200字；形如“模块名:实体名:功能名”；唯一")
    private String permissionCode;

    @Schema(description = "权限名称，非空；限200字")
    private String permissionName;

    @Schema(description = "菜单资源id，非空")
    private Long resourceId;

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
