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
import io.github.telechow.garoupa.api.entity.Role;
import io.github.telechow.garoupa.api.entity.RolePermissionRelation;
import io.github.telechow.garoupa.api.entity.RoleResourceRelation;
import io.github.telechow.garoupa.api.entity.UserRoleRelation;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.config.exception.ServiceException;
import io.github.telechow.garoupa.web.auto.service.IRoleAutoService;
import io.github.telechow.garoupa.web.auto.service.IRolePermissionRelationAutoService;
import io.github.telechow.garoupa.web.auto.service.IRoleResourceRelationAutoService;
import io.github.telechow.garoupa.web.auto.service.IUserRoleRelationAutoService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * 角色验证器
 *
 * @author Telechow
 * @since 2023/4/15 20:03
 */
@Component
@RequiredArgsConstructor
public class RoleValidator {

    private final IRoleAutoService roleAutoService;
    private final IUserRoleRelationAutoService userRoleRelationAutoService;
    private final IRoleResourceRelationAutoService roleResourceRelationAutoService;
    private final IRolePermissionRelationAutoService rolePermissionRelationAutoService;

    /**
     * 验证上级角色是否存在，如果不存在抛出异常
     *
     * @param parentId 上级角色id
     * @return io.github.telechow.garoupa.api.entity.Role 上级角色实体
     * @author Telechow
     * @since 2023/4/15 20:04
     */
    @Transactional(rollbackFor = Exception.class)
    public Role parentRoleExistById(@Nonnull Long parentId) {
        //1.如果上级角色id为0，则直接返回
        if (Objects.equals(parentId, 0L)) {
            return null;
        }

        //2.验证上级角色是否存在
        Role parentRole = roleAutoService.getById(parentId);
        return Optional.ofNullable(parentRole)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_PARENT_DATA_NOT_EXIST));
    }

    /**
     * 验证角色是否存在，如果不存在抛出异常
     *
     * @param id 角色id
     * @return io.github.telechow.garoupa.api.entity.Role 角色实体
     * @author Telechow
     * @since 2023/4/15 20:18
     */
    @Transactional(rollbackFor = Exception.class)
    public Role existById(@Nonnull Long id) {
        Role role = roleAutoService.getById(id);
        return Optional.ofNullable(role)
                .orElseThrow(() -> new ServiceException(ResponseCode.THE_SPECIFIED_DATA_NOT_EXIST));
    }

    /**
     * 验证角色是否包含下级角色，如果是则不允许删除
     *
     * @param id 角色id
     * @author Telechow
     * @since 2023/4/15 20:33
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteContainChildrenRole(@Nonnull Long id) {
        final long count = roleAutoService.count(Wrappers.<Role>lambdaQuery()
                .eq(Role::getParentId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.RBAC_ROLE_NOT_ALLOW_DELETE_WHEN_CONTAIN_CHILDREN_ROLE);
        }
    }

    /**
     * 验证角色是否关联了用户，如果是则不允许删除
     *
     * @param id 角色id
     * @author Telechow
     * @since 2023/4/15 21:08
     */
    @Transactional(rollbackFor = Exception.class)
    public void notAllowDeleteAssociationUser(@Nonnull Long id) {
        final long count = userRoleRelationAutoService.count(Wrappers.<UserRoleRelation>lambdaQuery()
                .eq(UserRoleRelation::getRoleId, id)
        );
        if (count != 0) {
            throw new ServiceException(ResponseCode.RBAC_ROLE_NOT_ALLOW_DELETE_WHEN_ASSOCIATION_USER);
        }
    }

    /**
     * 验证资源必须是此角色的上级角色拥有的资源，如果没有上级角色则可以关联所有资源
     *
     * @param parentId             上级角色id
     * @param resourceIdCollection 资源id集合
     * @author Telechow
     * @since 2023/4/15 23:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void parentRoleAlreadyAssociateResources(@Nonnull Long parentId, Collection<Long> resourceIdCollection) {
        //1.如果上级角色id为0，表示没有上级角色，直接返回
        if (Objects.equals(parentId, 0L)) {
            return;
        }

        //2.验证上级角色是否关联了所有指定的资源
        final long count = roleResourceRelationAutoService.count(Wrappers.<RoleResourceRelation>lambdaQuery()
                .eq(RoleResourceRelation::getRoleId, parentId)
                .in(RoleResourceRelation::getResourceId, resourceIdCollection)
        );
        if (!Objects.equals(count, (long) resourceIdCollection.size())) {
            throw new ServiceException(ResponseCode.RBAC_ROLE_ASSOCIATE_RESOURCE_OUT_OF_PARENT_ROLE_SCOPE);
        }
    }

    /**
     * 验证权限必须是此角色的上级角色拥有的权限，如果没有上级角色则可以关联所有权限
     *
     * @param parentId               上级角色id
     * @param permissionIdCollection 权限id集合
     * @author Telechow
     * @since 2023/4/15 23:04
     */
    @Transactional(rollbackFor = Exception.class)
    public void parentRoleAlreadyAssociatePermissions(@Nonnull Long parentId, Collection<Long> permissionIdCollection) {
        //1.如果上级角色id为0，表示没有上级角色，直接返回
        if (Objects.equals(parentId, 0L)) {
            return;
        }

        //2.验证上级角色是否关联了所有指定的权限
        final long count = rolePermissionRelationAutoService.count(Wrappers.<RolePermissionRelation>lambdaQuery()
                .eq(RolePermissionRelation::getRoleId, parentId)
                .in(RolePermissionRelation::getPermissionId, permissionIdCollection)
        );
        if (!Objects.equals(count, (long) permissionIdCollection.size())) {
            throw new ServiceException(ResponseCode.RBAC_ROLE_ASSOCIATE_PERMISSION_OUT_OF_PARENT_ROLE_SCOPE);
        }
    }

    /**
     * 验证角色是否全部存在，如果有一个不存在抛出异常
     *
     * @param idCollection 角色id集合
     * @author Telechow
     * @since 2023/4/16 16:50
     */
    @Transactional(rollbackFor = Exception.class)
    public void allExistByIdCollection(Collection<Long> idCollection) {
        final long count = roleAutoService.count(Wrappers.<Role>lambdaQuery()
                .in(Role::getId, idCollection)
        );
        if (!Objects.equals(count, (long) idCollection.size())) {
            throw new ServiceException(ResponseCode.RBAC_ROLE_AT_LEAST_ONE_NOT_EXIST);
        }
    }
}
