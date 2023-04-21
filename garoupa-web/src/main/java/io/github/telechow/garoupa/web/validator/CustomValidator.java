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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.rbac.user.UserTypeEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 客户验证器
 *
 * @author Telechow
 * @since 2023/4/17 17:12
 */
@Component
@RequiredArgsConstructor
public class CustomValidator {

    private final IUserAutoService userAutoService;

    /**
     * 验证客户是否存在，如果不存在抛出异常
     *
     * @param id 客户id
     * @return io.github.telechow.garoupa.api.entity.User 客户实体
     * @author Telechow
     * @since 2023/4/17 17:13
     */
    @Transactional(rollbackFor = Exception.class)
    public User existById(@Nonnull Long id){
        User user = userAutoService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, id)
                .eq(User::getUserType, UserTypeEnum.CUSTOM.getCode())
                .last("limit 1")
        );
        return Optional.ofNullable(user)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }
}
