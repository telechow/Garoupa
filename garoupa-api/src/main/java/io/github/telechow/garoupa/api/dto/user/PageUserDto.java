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

import io.github.telechow.garoupa.api.dto.common.CommonPageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询用户dto
 *
 * @author Telechow
 * @since 2023/4/16 17:37
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分页查询用户dto")
public class PageUserDto extends CommonPageDto {

    @Schema(description = "用户名模糊查询条件")
    @Size(max = 50, message = "用户名模糊查询条件太长")
    private String userNameLike;

    @Schema(description = "用户昵称模糊查询条件")
    @Size(max = 50, message = "用户昵称模糊查询条件太长")
    private String nickNameLike;

    @Schema(description = "用户身份证号查询条件")
    @Size(min = 18, max = 18, message = "请输入正确的身份证号")
    private String idCardNumber;

    @Schema(description = "电话号码查询条件")
    @Size(max = 50, message = "电话号码查询条件太长")
    private String mobile;

    @Schema(description = "邮箱查询条件")
    @Size(max = 50, message = "邮箱查询条件太长")
    private String email;
}
