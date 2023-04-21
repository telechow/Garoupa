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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.ahoo.cosid.annotation.CosId;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author telechow
 * @since 2023-03-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("garoupa_user")
@Schema(name = "User", description = "用户表")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @CosId(value = "User")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Schema(description = "用户名，非空；限50字；唯一")
    private String userName;

    @Schema(description = "散列后的密码，非空")
    private String password;

    @Schema(description = "用户昵称，非空；限50字")
    private String nickName;

    @Schema(description = "头像uri；限2000字")
    private String avatarUri;

    @Schema(description = "用户真实姓名；限50字")
    private String realName;

    @Schema(description = "用户身份证号；限18字")
    private String idCardNumber;

    @Schema(description = "电话号码；限50字")
    private String mobile;

    @Schema(description = "邮箱；限50字")
    private String email;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "性别；0：男，10：女，20：未知")
    private Integer gender;

    @Schema(description = "用户类型，非空；0：用户，10：客户")
    private Integer userType;

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
