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
package io.github.telechow.garoupa.api.dto.user;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户修改自己的信息dto
 *
 * @author Telechow
 * @since 2023/4/17 16:24
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户修改自己的信息dto")
public class UpdateSelfDto implements Serializable {

    @Schema(description = "用户昵称，非空；限50字")
    @NotBlank(message = "你用昵称不能为空")
    @Size(max = 50, message = "用户昵称太长")
    private String nickName;

    @Schema(description = "头像uri；限2000字")
    @Size(max = 2000,message = "用户头像uri太长")
    private String avatarUri;

    @Schema(description = "用户真实姓名；限50字")
    @Size(max = 50,message = "用户真实姓名太长")
    private String realName;

    @Schema(description = "用户身份证号；限18字")
    @Size(min = 18,max = 18,message = "用户身份证号必须是18位")
    private String idCardNumber;

    @Schema(description = "电话号码；限50字")
    @Size(max = 50,message = "电话号码太长")
    private String mobile;

    @Schema(description = "邮箱；限50字")
    @Size(max = 50,message = "邮箱太长")
    private String email;

    @Schema(description = "生日")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "性别；0：男，10：女，20：未知")
    private Integer gender;
}
