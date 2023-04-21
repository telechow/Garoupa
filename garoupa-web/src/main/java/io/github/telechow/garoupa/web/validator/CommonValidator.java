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
package io.github.telechow.garoupa.web.validator;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PhoneUtil;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 通用验证器
 *
 * @author Telechow
 * @since 2023/4/12 13:58
 */
@Component
@RequiredArgsConstructor
public class CommonValidator {

    private final SystemDictHelper systemDictHelper;

    /**
     * 验证系统字典项的值是否在系统字典中存在
     *
     * @param systemDictEnum 系统字典枚举
     * @param itemValue      系统字典项的值
     * @author Telechow
     * @since 2023/4/12 23:54
     */
    public void validateSystemDictItemValueExist(@Nonnull SystemDictEnum systemDictEnum, @Nonnull String itemValue) {
        SystemDictItem systemDictItem = systemDictHelper.getByDictCodeAndItemValue(
                systemDictEnum.getDictCode(), itemValue);
        Optional.ofNullable(systemDictItem)
                .orElseThrow(() -> new ServiceException(ResponseCode.REQUEST_PARAMETER_ERROR));
    }

    /**
     * 验证身份证号是否符合国标
     * <li>根据GB11643-1999来验证</li>
     *
     * @param idCardNumber 身份证号
     * @author Telechow
     * @since 2023/4/17 16:35
     */
    public void validateIdCardNumber(String idCardNumber) {
        if (!IdcardUtil.isValidCard18(idCardNumber)) {
            throw new ServiceException(ResponseCode.ID_CARD_NUMBER_VALIDATE_ERROR);
        }
    }

    /**
     * 验证电话号码是否合法
     * <li>是否为座机号码+手机号码（中国大陆）+手机号码（中国香港）+手机号码（中国台湾）+手机号码（中国澳门）</li>
     *
     * @param mobile 电话号码
     * @author Telechow
     * @since 2023/4/17 16:45
     */
    public void validateMobile(String mobile) {
        if (!PhoneUtil.isPhone(mobile)) {
            throw new ServiceException(ResponseCode.MOBILE_VALIDATE_ERROR);
        }
    }

    /**
     * 验证电子邮箱是否合法
     * <li>根据RFC 5322规范来验证</li>
     *
     * @param email 电子邮箱
     * @author Telechow
     * @since 2023/4/17 16:48
     */
    public void validateEmail(String email) {
        if (!Validator.isEmail(email)) {
            throw new ServiceException(ResponseCode.EMAIL_VALIDATE_ERROR);
        }
    }
}
