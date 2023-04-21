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

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户修改自己的密码dto
 *
 * @author Telechow
 * @since 2023/4/16 20:16
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户修改自己的密码dto")
public class UpdatePasswordSelfDto implements Serializable {

    @Schema(description = "原始密码，非空")
    @NotBlank(message = "原始密码不能为空")
    private String originPassword;

    @Schema(description = "新密码，非空")
    @NotBlank(message = "新密码不能为空")
    private String neoPassword;

    @Schema(description = "再次输入新密码，非空")
    @NotBlank(message = "再次输入的新密码不能为空")
    private String neoPasswordAgain;
}
