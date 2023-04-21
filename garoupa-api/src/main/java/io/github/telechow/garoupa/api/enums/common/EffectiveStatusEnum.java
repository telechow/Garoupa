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
package io.github.telechow.garoupa.api.enums.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 有效状态枚举
 *
 * @author Telechow
 * @since 2023/4/11 22:14
 */
@Getter
@AllArgsConstructor
public enum EffectiveStatusEnum {

    /**
     * 无效
     */
    INEFFECTIVE(0),

    /**
     * 有效
     */
    EFFECTIVE(10),
    ;

    private final Integer code;
}
