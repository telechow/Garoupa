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
package io.github.telechow.garoupa.api.vo.user;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户分页vo
 *
 * @author Telechow
 * @since 2023/4/16 17:27
 */
@Data
@Accessors(chain = true)
@Schema(description = "用户分页vo")
public class UserPageVo implements Serializable {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名，非空；限50字；唯一")
    private String userName;

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
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "性别；0：男，10：女，20：未知")
    private Integer gender;

    @Schema(description = "性别名称")
    private String genderName;

    @Schema(description = "创建时间，非空")
    private LocalDateTime createTime;
}
