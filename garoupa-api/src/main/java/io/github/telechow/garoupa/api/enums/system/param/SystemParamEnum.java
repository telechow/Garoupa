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
package io.github.telechow.garoupa.api.enums.system.param;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统参数枚举
 *
 * @author Telechow
 * @since 2023/4/8 23:32
 */
@Getter
@AllArgsConstructor
public enum SystemParamEnum {

    ///############################################################ JWT参数

    /**
     * Jwt默认过期时间（单位秒）
     */
    EXPIRE_SECOND("jwt.expireSecond", "2592000"),

    ///############################################################ RBAC参数

    /**
     * 用户默认密码
     */
    RBAC_USER_DEFAULT_PASSWORD("rbac.userDefaultPassword", "Garoupa2023"),

    /**
     * 用户默认昵称前缀
     */
    RBAC_USER_DEFAULT_NICKNAME_PREFIX("rbac.userDefaultNicknamePrefix", "管理员"),

    /**
     * 用户密码校验正则表达式
     */
    RBAC_USER_PASSWORD_VALID_REGULAR_EXPRESSION("rbac.userPasswordValidRegularExpression", "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50}$"),

    /**
     * 用户密码校验正则表达式文字要求
     */
    RBAC_USER_PASSWORD_VALID_REGULAR_EXPRESSION_TEXT_REQUIREMENT("rbac.userPasswordValidRegularExpressionTextRequirement", "密码至少包含一个大写字母，一个小写字母，一个数字，长度8-50位"),
    ;

    private final String code;
    private final String defaultValue;
}
