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
package io.github.telechow.garoupa.api.vo.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录成功vo
 *
 * @author Telechow
 * @since 2023/3/29 11:50
 */
@Data
@Accessors(chain = true)
@Schema(name = "登录成功vo", description = "登录成功vo")
public class LoginVo implements Serializable {

	@Schema(description = "用户id")
	private Long id;

	@Schema(description = "用户名，非空；限50字；唯一")
	private String userName;

	@Schema(description = "用户昵称，非空；限50字")
	private String nickName;

	@Schema(description = "头像uri；限2000字")
	private String avatarUri;

	@Schema(description = "创建时间，非空")
	private LocalDateTime createTime;

	@Schema(description = "拥有的资源（菜单和按钮）编码列表")
	private List<String> resourceCodeList;
}
