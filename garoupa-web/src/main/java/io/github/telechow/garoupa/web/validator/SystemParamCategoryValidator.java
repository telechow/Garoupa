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
import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.api.entity.SystemParamCategory;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.ISystemParamAutoService;
import io.github.telechow.garoupa.web.auto.service.ISystemParamCategoryAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

/**
 * 系统参数分类验证器
 *
 * @author Telechow
 * @since 2023/4/7 14:43
 */
@Component
@RequiredArgsConstructor
public class SystemParamCategoryValidator {

    private final ISystemParamCategoryAutoService systemParamCategoryAutoService;
    private final ISystemParamAutoService systemParamAutoService;

    /**
     * 根据 系统参数分类id 验证 系统参数分类实体 是否存在，如果不存在抛出异常
     *
     * @param id 系统参数分类id
     * @return io.github.telechow.garoupa.api.entity.SystemParamCategory 系统参数分类实体
     * @author Telechow
     * @since 2023/4/7 14:45
     */
    @Transactional(rollbackFor = Exception.class)
    public SystemParamCategory existById(@Nonnull Long id) {
        SystemParamCategory systemParamCategory = systemParamCategoryAutoService.getById(id);
        return Optional.ofNullable(systemParamCategory)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证系统参数分类是否被锁定，如果被锁定抛出异常
     *
     * @param systemParamCategory 系统参数分类实体
     * @author Telechow
     * @since 2023/4/8 22:29
     */
    public void isLocked(@Nonnull SystemParamCategory systemParamCategory) {
        if (Objects.equals(systemParamCategory.getLockSign(), LockSignEnum.LOCKED.getCode())) {
            throw new ServiceException(ResponseCode.SYSTEM_PARAM_CATEGORY_LOCKED);
        }
    }

    /**
     * 验证系统参数分类中存在系统参数，则不允许删除
     *
     * @param id 系统参数分类id
     * @author Telechow
     * @since 2023/4/10 15:57
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteContainSystemParam(@Nonnull Long id) {
        final long count = systemParamAutoService.count(Wrappers.<SystemParam>lambdaQuery()
                .eq(SystemParam::getParamCategoryId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.SYSTEM_PARAM_CATEGORY_NOT_ALLOW_DELETE_WHEN_CONTAIN_SYSTEM_PARAM);
        }
    }
}
