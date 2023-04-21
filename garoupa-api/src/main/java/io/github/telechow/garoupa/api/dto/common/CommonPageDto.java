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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 通用分页dto
 *
 * @author Telechow
 * @since 2023/4/7 22:39
 */
@Data
@Accessors(chain = true)
@Schema(description = "通用分页dto")
public class CommonPageDto implements Serializable {

    @Schema(description = "页码")
    @NotNull(message = "页码不能为空")
    @Positive(message = "页码必须为正整数")
    @Max(value = 1000, message = "页码太大")
    private Long current;

    @Schema(description = "每页条数")
    @NotNull(message = "每页条数不能为空")
    @Positive(message = "每页条数必须为正整数")
    @Max(value = 100, message = "每页条数太大")
    private Long size;
}
