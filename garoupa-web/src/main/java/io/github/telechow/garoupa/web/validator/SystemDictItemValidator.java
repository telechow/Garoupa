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

import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 系统字典项验证器
 *
 * @author Telechow
 * @since 2023/4/11 23:36
 */
@Component
@RequiredArgsConstructor
public class SystemDictItemValidator {

    private final ISystemDictItemAutoService systemDictItemAutoService;

    /**
     * 验证系统字典项是否存在，如果不存在抛出异常
     *
     * @param id 系统字典项id
     * @return io.github.telechow.garoupa.api.entity.SystemDictItem 系统字典项实体
     * @author Telechow
     * @since 2023/4/11 23:36
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemDictItem existById(@Nonnull Long id) {
        SystemDictItem systemDictItem = systemDictItemAutoService.getById(id);
        return Optional.ofNullable(systemDictItem)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证系统字典项是否被锁定，如果是抛出异常不允许修改
     *
     * @param systemDictItem 系统字典项实体
     * @author Telechow
     * @since 2023/4/11 23:38
     */
    public void isLocked(@Nonnull SystemDictItem systemDictItem){
        if (Objects.equals(systemDictItem.getLockSign(), LockSignEnum.LOCKED.getCode())) {
            throw new ServiceException(ResponseCode.SYSTEM_DICT_ITEM_LOCKED);
        }
    }
}
