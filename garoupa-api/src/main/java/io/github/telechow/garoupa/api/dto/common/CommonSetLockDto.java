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
package io.github.telechow.garoupa.api.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用设置锁定dto
 *
 * @author Telechow
 * @since 2023/4/11 16:04
 */
@Data
@Accessors(chain = true)
@Schema(description = "通用设置锁定dto")
public class CommonSetLockDto implements Serializable {

    @Schema(description = "锁定标识，非空；false：未锁定，true：锁定")
    @NotNull(message = "锁定标识不能为空")
    private Boolean lockSign;
}
