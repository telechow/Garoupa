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

import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.ISystemParamAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 系统参数验证器
 *
 * @author Telechow
 * @since 2023/4/7 21:36
 */
@Component
@RequiredArgsConstructor
public class SystemParamValidator {

    private final ISystemParamAutoService systemParamAutoService;

    /**
     * 根据 系统参数id 验证 系统参数实体 是否存在，如果不存在抛出异常
     *
     * @param id 系统参数id
     * @return io.github.telechow.garoupa.api.entity.SystemParam 系统参数实体
     * @author Telechow
     * @since 2023/4/7 21:42
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemParam existById(@Nonnull Long id) {
        SystemParam systemParam = systemParamAutoService.getById(id);
        return Optional.ofNullable(systemParam)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证系统参数是否是否被锁定，如果是抛出异常
     *
     * @param systemParam 系统参数实体
     * @author Telechow
     * @since 2023/4/7 21:45
     */
    public void isLocked(@Nonnull SystemParam systemParam) {
        if (Objects.equals(systemParam.getLockSign(), LockSignEnum.LOCKED.getCode())) {
            throw new ServiceException(ResponseCode.SYSTEM_PARAM_LOCKED);
        }
    }
}
