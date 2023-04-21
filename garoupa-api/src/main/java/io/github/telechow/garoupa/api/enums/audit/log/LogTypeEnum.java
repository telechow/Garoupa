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
package io.github.telechow.garoupa.api.enums.audit.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志类型枚举
 *
 * @author Telechow
 * @since 2023/4/20 9:07
 */
@Getter
@AllArgsConstructor
public enum LogTypeEnum {

    /**
     * 正常
     */
    NORMAL(0),

    /**
     * 错误
     */
    ERROR(10),
    ;

    private final Integer code;
}
