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
 * 系统字典项分页vo
 *
 * @author Telechow
 * @since 2023/4/12 15:07
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统字典项分页vo")
public class SystemDictItemPageVo implements Serializable {

    @Schema(description = "系统字典项id")
    private Long id;

    @Schema(description = "系统字典id，非空")
    private Long dictId;

    @Schema(description = "系统字典编码，冗余；限250字，小写下划线命名法")
    private String dictCode;

    @Schema(description = "系统字典名称，冗余；限250字")
    private String dictName;

    @Schema(description = "字典项值，非空；限50字")
    private String itemValue;

    @Schema(description = "字典项文本，非空；限50字")
    private String itemText;

    @Schema(description = "排序，非空；小号在前")
    private Integer sortNumber;

    @Schema(description = "有效状态，非空；0：无效，10：有效")
    private Integer effectiveStatus;

    @Schema(description = "有效状态名称")
    private String effectiveStatusName;

    @Schema(description = "锁定标识，非空；false；未锁定，true：已锁定")
    private Boolean lockSign;

    @Schema(description = "锁定标识名称")
    private String lockSignName;
}
