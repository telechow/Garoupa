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
import io.github.telechow.garoupa.api.entity.Resource;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.rbac.resource.ResourceTypeEnum;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import io.github.telechow.garoupa.web.auto.service.IResourceAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * 资源验证器
 *
 * @author Telechow
 * @since 2023/4/13 16:20
 */
@Component
@RequiredArgsConstructor
public class ResourceValidator {

    private final IResourceAutoService resourceAutoService;
    private final IPermissionAutoService permissionAutoService;

    /**
     * 验证上级资源是否存在且为菜单，如果不满足条件抛出异常
     *
     * @param parentId 上级资源id
     * @return io.github.telechow.garoupa.api.entity.Resource 上级资源实体
     * @author Telechow
     * @since 2023/4/13 16:21
     */
    @Transactional(rollbackFor = Exception.class)
    public Resource parentExistAndIsMenuById(@Nonnull Long parentId) {
        //1.如果上级资源id为0，则表示没有上级资源，直接返回null
        if (Objects.equals(parentId, 0L)) {
            return null;
        }

        //2.验证上级资源是否存在
        Resource resource = resourceAutoService.getById(parentId);
        Optional.ofNullable(resource)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_PARENT_DATA_NOT_EXIST));

        //3.验证上级资源是否为菜单
        if (!Objects.equals(resource.getResourceType(), ResourceTypeEnum.MENU.getCode())) {
            throw new ServiceException(ResponseCode.RBAC_RESOURCE_PARENT_MUST_A_MENU);
        }
        return resource;
    }

    /**
     * 验证资源是否存在，如果不存在抛出异常
     *
     * @param id 资源id
     * @return io.github.telechow.garoupa.api.entity.Resource 资源实体
     * @author Telechow
     * @since 2023/4/13 17:58
     */
    @Transactional(rollbackFor = Exception.class)
    public Resource existById(@Nonnull Long id) {
        Resource resource = resourceAutoService.getById(id);
        return Optional.ofNullable(resource)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证资源是否包含下级资源，如果是则不允许删除
     *
     * @param id 资源id
     * @author Telechow
     * @since 2023/4/13 18:11
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteContainChildrenResource(@Nonnull Long id) {
        final long count = resourceAutoService.count(Wrappers.<Resource>lambdaQuery()
                .eq(Resource::getParentId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.RBAC_RESOURCE_NOT_ALLOW_DELETE_WHEN_CONTAIN_CHILDREN_RESOURCE);
        }
    }

    /**
     * 验证资源是否关联权限，如果是则不允许删除
     *
     * @param id 资源id
     * @author Telechow
     * @since 2023/4/13 18:17
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteAssociationPermission(@Nonnull Long id) {
        final long count = permissionAutoService.count(Wrappers.<Permission>lambdaQuery()
                .eq(Permission::getResourceId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.RBAC_RESOURCE_NOT_ALLOW_DELETE_WHEN_ASSOCIATION_PERMISSION);
        }
    }

    /**
     * 根据菜单id验证菜单是否存在，如果不存在抛出异常
     *
     * @param menuId 菜单id
     * @return io.github.telechow.garoupa.api.entity.Resource 菜单资源实体
     * @author Telechow
     * @since 2023/4/14 15:36
     */
    @Transactional(rollbackFor = Exception.class)
    public Resource menuExistById(@Nonnull Long menuId) {
        Resource menu = resourceAutoService.getOne(Wrappers.<Resource>lambdaQuery()
                .eq(Resource::getId, menuId)
                .eq(Resource::getResourceType, ResourceTypeEnum.MENU.getCode())
                .last("limit 1")
        );
        return Optional.ofNullable(menu)
                .orElseThrow(() -> new ServiceException(ResponseCode.RBAC_MENU_RESOURCE_NOT_EXIST));
    }

    /**
     * 验证资源是否全部存在，如果有一个不存在抛出异常
     *
     * @param idCollection 资源id集合，非空
     * @author Telechow
     * @since 2023/4/15 22:56
     */
    @Transactional(rollbackFor = Exception.class)
    public void allExistByIdCollection(Collection<Long> idCollection) {
        final long count = resourceAutoService.count(Wrappers.<Resource>lambdaQuery()
                .in(Resource::getId, idCollection)
        );
        if (!Objects.equals(count, (long) idCollection.size())) {
            throw new ServiceException(ResponseCode.RBAC_RESOURCE_AT_LEAST_ONE_NOT_EXIST);
        }
    }
}
