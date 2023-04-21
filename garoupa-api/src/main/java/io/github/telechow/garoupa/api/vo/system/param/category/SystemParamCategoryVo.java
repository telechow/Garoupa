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
package io.github.telechow.garoupa.api.vo.system.param.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统参数分类vo
 *
 * @author Telechow
 * @since 2023/4/7 16:42
 */
@Data
@Accessors(chain = true)
@Schema(description = "系统参数分类vo")
public class SystemParamCategoryVo implements Serializable {

    @Schema(description = "系统参数分类id")
    private Long id;

    @Schema(description = "系统参数分类名称，非空，唯一；限50字")
    private String categoryName;

    @Schema(description = "锁定标识，如果是锁定的系统参数分类，则不允许删除，非空；false：未锁定，true：锁定")
    private Boolean lockSign;

    @Schema(description = "锁定标识名称")
    private String lockSignName;
}
