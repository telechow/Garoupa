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

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.rbac.user.UserTypeEnum;
import io.github.telechow.garoupa.api.enums.system.param.SystemParamEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import io.github.telechow.garoupa.web.helper.SystemParamHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 用户验证器
 *
 * @author Telechow
 * @since 2023/4/16 16:30
 */
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final IUserAutoService userAutoService;

    private final SystemParamHelper systemParamHelper;

    private final PasswordEncoder passwordEncoder;

    /**
     * 验证用户是否存在，如果不存在抛出异常
     *
     * @param id 用户id
     * @return io.github.telechow.garoupa.api.entity.User 用户实体
     * @author Telechow
     * @since 2023/4/16 16:30
     */
    @Transactional(rollbackFor = Exception.class)
    public User existById(@Nonnull Long id) {
        User user = userAutoService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, id)
                .eq(User::getUserType, UserTypeEnum.USER.getCode())
                .last("limit 1")
        );
        return Optional.ofNullable(user)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证用户类型为用户，如果不是抛出异常
     *
     * @param user 用户实体
     * @author Telechow
     * @since 2023/4/16 20:24
     */
    public void userTypeIsUser(@Nullable User user) {
        //1.如果用户实体为空，抛出异常
        if (Objects.isNull(user)) {
            throw new ServiceException(ResponseCode.ACCESS_DENIED);
        }

        //2.验证用户类型为用户
        if (!Objects.equals(user.getUserType(), UserTypeEnum.USER.getCode())) {
            throw new ServiceException(ResponseCode.ACCESS_DENIED);
        }
    }

    /**
     * 验证修改密码
     *
     * @param neoPassword      新密码
     * @param neoPasswordAgain 再次输入的新密码
     * @param originPassword   原始密码
     * @author Telechow
     * @since 2023/4/16 20:28
     */
    public void validateUpdatePassword(String neoPassword, String neoPasswordAgain
            , String originPassword, User user) {
        //1.两次输入的新密码是否一致
        if (!StrUtil.equals(neoPassword, neoPasswordAgain)) {
            throw new ServiceException(ResponseCode.TWO_NEO_PASSWORD_NOT_MATCH);
        }

        //2.原始密码是否匹配
        if (!passwordEncoder.matches(originPassword, user.getPassword())) {
            throw new ServiceException(ResponseCode.ORIGIN_PASSWORD_NOT_MATCH);
        }

        //3.新密码是否与系统参数的正则表达式相匹配
        String expression = systemParamHelper.getStringValue(SystemParamEnum.RBAC_USER_PASSWORD_VALID_REGULAR_EXPRESSION);
        String text = systemParamHelper.getStringValue(SystemParamEnum.RBAC_USER_PASSWORD_VALID_REGULAR_EXPRESSION_TEXT_REQUIREMENT);
        if (!Pattern.matches(expression, neoPassword)) {
            throw new ServiceException(ResponseCode.NEO_PASSWORD_NOT_SATISFY_REQUIREMENT, text);
        }
    }
}
