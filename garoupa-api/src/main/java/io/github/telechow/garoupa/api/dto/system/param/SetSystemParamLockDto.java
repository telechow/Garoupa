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
package io.github.telechow.garoupa.api.dto.system.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 设置系统参数锁定标识dto
 *
 * @author Telechow
 * @since 2023/4/8 19:49
 */
@Data
@Accessors(chain = true)
@Schema(description = "设置系统参数锁定标识dto")
public class SetSystemParamLockDto implements Serializable {

    @Schema(description = "锁定标识，如果是锁定的系统参数，则不允许删除，非空；false：未锁定，true：锁定")
    @NotNull(message = "锁定标识不能为空")
    private Boolean lockSign;
}
