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
 * 登录日志表
 * </p>
 *
 * @author telechow
 * @since 2023-04-18
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("garoupa_login_log")
@Schema(name = "LoginLog", description = "登录日志表")
public class LoginLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "登录日志id")
    @CosId(value = "LoginLog")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @Schema(description = "用户id，如果登录失败则为null")
    private Long userId;

    @Schema(description = "登录方式，非空")
    private String loginMode;

    @Schema(description = "是否登录成功，非空；false：失败，true：成功")
    private Boolean isSuccess;

    @Schema(description = "用户ip，非空")
    private String remoteIp;

    @Schema(description = "用户代理，非空")
    private String userAgent;

    @Schema(description = "登录参数，非空；将多个参数转换成json")
    private String loginParam;

    @Schema(description = "创建时间，非空")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
