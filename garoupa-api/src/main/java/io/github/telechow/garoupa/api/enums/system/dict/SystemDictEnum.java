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
package io.github.telechow.garoupa.api.enums.system.dict;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统字典枚举
 *
 * @author Telechow
 * @since 2023/4/12 13:53
 */
@Getter
@AllArgsConstructor
public enum SystemDictEnum {

    ///############################################################ 通用系统字典

    /**
     * 锁定标识
     */
    LOCK_SIGN("lock_sign"),

    /**
     * 有效状态
     */
    EFFECTIVE_STATUS("effective_status"),

    /**
     * 性别
     */
    GENDER("gender"),

    /**
     * 是否
     */
    TRUE_FALSE("true_false"),

    ///############################################################ RBAC相关系统字典

    /**
     * 资源类型
     */
    RBAC_RESOURCE_TYPE("rbac_resource_type"),

    /**
     * 用户类型
     */
    RBAC_USER_TYPE("rbac_user_type"),

    ///############################################################ 审计日志相关系统字典

    /**
     * 审计日志日志类型
     */
    AUDIT_LOG_LOG_TYPE("audit_log_log_type"),
    ;

    private final String dictCode;
}
