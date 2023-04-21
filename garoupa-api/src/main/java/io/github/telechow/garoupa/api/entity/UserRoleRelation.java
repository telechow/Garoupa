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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.ahoo.cosid.annotation.CosId;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author telechow
 * @since 2023-03-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("garoupa_user_role_relation")
@Schema(name = "UserRoleRelation", description = "用户角色关系表")
public class UserRoleRelation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户角色关系表")
    @CosId(value = "UserRoleRelation")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Schema(description = "用户id，非空")
    private Long userId;

    @Schema(description = "角色id，非空")
    private Long roleId;
}
