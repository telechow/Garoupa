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
 * 通用设置有效状态dto
 *
 * @author Telechow
 * @since 2023/4/12 13:48
 */
@Data
@Accessors(chain = true)
@Schema(description = "通用设置有效状态dto")
public class CommonSetEffectiveStatusDto implements Serializable {

    @Schema(description = "有效状态，非空；0：无效，10：有效")
    @NotNull(message = "有效状态不能为空")
    private Integer effectiveStatus;
}
