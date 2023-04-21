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
package io.github.telechow.garoupa.api.vo.system.dict;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统字典分页vo
 *
 * @author Telechow
 * @since 2023/4/11 17:02
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统字典分页vo")
public class SystemDictPageVo implements Serializable {

    @Schema(description = "系统字典id")
    private Long id;

    @Schema(description = "字典编码，非空；限250字，小写下划线命名法")
    private String dictCode;

    @Schema(description = "字典名称，非空；限250字")
    private String dictName;

    @Schema(description = "字典描述；限1000字")
    private String dictDescribe;

    @Schema(description = "排序，非空；小号在前")
    private Integer sortNumber;

    @Schema(description = "锁定标识，非空；false：未锁定，true：已锁定")
    private Boolean lockSign;

    @Schema(description = "锁定标识名称")
    private String lockSignName;
}
