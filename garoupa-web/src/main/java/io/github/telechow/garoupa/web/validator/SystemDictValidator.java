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
import io.github.telechow.garoupa.api.entity.SystemDict;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.ISystemDictAutoService;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 系统字典验证器
 *
 * @author Telechow
 * @since 2023/4/10 23:09
 */
@Component
@RequiredArgsConstructor
public class SystemDictValidator {

    private final ISystemDictAutoService systemDictAutoService;
    private final ISystemDictItemAutoService systemDictItemAutoService;

    /**
     * 根据 系统字典id 验证 系统字典 是否存在，如果不存在抛出异常
     *
     * @param id 系统字典id
     * @return io.github.telechow.garoupa.api.entity.SystemDict 系统字典
     * @author Telechow
     * @since 2023/4/10 23:10
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemDict existById(@Nonnull Long id) {
        SystemDict systemDict = systemDictAutoService.getById(id);
        return Optional.ofNullable(systemDict)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证系统字典是否被锁定，如果被锁定抛出异常
     *
     * @param systemDict 系统字典实体
     * @author Telechow
     * @since 2023/4/10 23:53
     */
    public void isLocked(@Nonnull SystemDict systemDict) {
        if (Objects.equals(systemDict.getLockSign(), LockSignEnum.LOCKED.getCode())) {
            throw new ServiceException(ResponseCode.SYSTEM_DICT_LOCKED);
        }
    }

    /**
     * 验证系统字典中存在系统字典项，则不允许删除
     *
     * @param id 系统字典id
     * @author Telechow
     * @since 2023/4/11 15:55
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteContainSystemDictItem(@Nonnull Long id) {
        final long count = systemDictItemAutoService.count(Wrappers.<SystemDictItem>lambdaQuery()
                .eq(SystemDictItem::getDictId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.SYSTEM_DICT_NOT_ALLOW_DELETE_WHEN_CONTAIN_SYSTEM_DICT_ITEM);
        }
    }
}
