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
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * 权限验证器
 *
 * @author Telechow
 * @since 2023/4/14 15:43
 */
@Component
@RequiredArgsConstructor
public class PermissionValidator {

    private final IPermissionAutoService permissionAutoService;

    /**
     * 验证权限是否存在，如果不存在抛出异常
     *
     * @param id 权限id
     * @return io.github.telechow.garoupa.api.entity.Permission 权限实体
     * @author Telechow
     * @since 2023/4/14 15:44
     */
    @Transactional(rollbackFor = Exception.class)
    public Permission existById(@Nonnull Long id) {
        Permission permission = permissionAutoService.getById(id);
        return Optional.ofNullable(permission)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证权限是否全部存在，如果有一个不存在抛出异常
     *
     * @param idCollection 权限id集合，非空
     * @author Telechow
     * @since 2023/4/15 22:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void allExistByIdCollection(Collection<Long> idCollection) {
        final long count = permissionAutoService.count(Wrappers.<Permission>lambdaQuery()
                .in(Permission::getId, idCollection)
        );
        if (!Objects.equals(count, (long) idCollection.size())) {
            throw new ServiceException(ResponseCode.RBAC_PERMISSION_AT_LEAST_ONE_NOT_EXIST);
        }
    }
}
