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
package io.github.telechow.garoupa.api.vo.system.dict.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统字典项vo
 *
 * @author Telechow
 * @since 2023/4/12 15:52
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统字典项vo")
public class SystemDictItemVo implements Serializable {

    @Schema(description = "系统字典项id")
    private Long id;

    @Schema(description = "字典项值，非空；限50字")
    private String itemValue;

    @Schema(description = "字典项文本，非空；限50字")
    private String itemText;
}
