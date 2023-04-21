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
package io.github.telechow.garoupa.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.AuthenticationCacheConstant;
import io.github.telechow.garoupa.api.constant.RbacCacheConstant;
import io.github.telechow.garoupa.api.dto.role.CreateRoleDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociatePermissionDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociateResourcesDto;
import io.github.telechow.garoupa.api.dto.role.UpdateRoleDto;
import io.github.telechow.garoupa.api.entity.Role;
import io.github.telechow.garoupa.api.entity.RolePermissionRelation;
import io.github.telechow.garoupa.api.entity.RoleResourceRelation;
import io.github.telechow.garoupa.web.auto.service.IRoleAutoService;
import io.github.telechow.garoupa.web.auto.service.IRolePermissionRelationAutoService;
import io.github.telechow.garoupa.web.auto.service.IRoleResourceRelationAutoService;
import io.github.telechow.garoupa.web.service.IRoleService;
import io.github.telechow.garoupa.web.validator.PermissionValidator;
import io.github.telechow.garoupa.web.validator.ResourceValidator;
import io.github.telechow.garoupa.web.validator.RoleValidator;
import io.github.telechow.garoupa.web.wrapper.RolePermissionRelationWrapper;
import io.github.telechow.garoupa.web.wrapper.RoleResourceRelationWrapper;
import io.github.telechow.garoupa.web.wrapper.RoleWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

/**
 * 角色service实现
 *
 * @author Telechow
 * @since 2023/4/15 20:00
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleValidator roleValidator;
    private final ResourceValidator resourceValidator;
    private final PermissionValidator permissionValidator;

    private final RoleWrapper roleWrapper;
    private final RoleResourceRelationWrapper roleResourceRelationWrapper;
    private final RolePermissionRelationWrapper rolePermissionRelationWrapper;

    private final IRoleAutoService roleAutoService;
    private final IRoleResourceRelationAutoService roleResourceRelationAutoService;
    private final IRolePermissionRelationAutoService rolePermissionRelationAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateRoleDto dto) {
        //1.数据验证
        //1.1.验证上级角色是否存在，如果不存在抛出异常
        roleValidator.parentRoleExistById(dto.getParentId());

        //2.实例化角色实体，并入库
        Role role = roleWrapper.instance(dto);
        roleAutoService.save(role);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdateRoleDto dto) {
        //1.数据验证
        //1.1.验证角色是否存在，如果不存在抛出异常
        roleValidator.existById(id);

        //2.修改角色实体，并入库
        Role updateRole = new Role();
        updateRole.setId(id).setRoleCode(dto.getRoleCode()).setRoleName(dto.getRoleName());
        roleAutoService.updateById(updateRole);

        //3.清除缓存，因为此接口只能修改角色的名称和编码，所以不需要清除用户和角色关联而生成的缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证角色是否存在，如果不存在抛出异常
        roleValidator.existById(id);
        //1.2.验证角色是否包含下级角色，如果是则不允许删除
        roleValidator.notAllowDeleteContainChildrenRole(id);
        //1.3.验证角色是否关联了用户，如果是则不允许删除
        roleValidator.notAllowDeleteAssociationUser(id);

        //2.逻辑删除角色
        roleAutoService.removeById(id);

        //3.删除角色资源关系实体和角色权限关系实体
        roleResourceRelationAutoService.remove(Wrappers.<RoleResourceRelation>lambdaQuery()
                .eq(RoleResourceRelation::getRoleId, id)
        );
        rolePermissionRelationAutoService.remove(Wrappers.<RolePermissionRelation>lambdaQuery()
                .eq(RolePermissionRelation::getRoleId, id)
        );

        //4.清除缓存，因为如果用户关联了角色是不允许删除角色的，所以不需要清除用户和角色关联而生成的缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateResources(long id, RoleAssociateResourcesDto dto) {
        //1.数据验证
        //1.1.验证角色是否存在，如果不存在抛出异常
        Role role = roleValidator.existById(id);

        //2.删除角色资源关系实体
        roleResourceRelationAutoService.remove(Wrappers.<RoleResourceRelation>lambdaQuery()
                .eq(RoleResourceRelation::getRoleId, id)
        );

        try {
            //3.如果传递的资源id集合为空，则什么都不用做，直接返回
            if (CollectionUtil.isEmpty(dto.getResourceIds())) {
                return;
            }

            //4.再次进行数据验证
            //4.1.验证资源是否全部存在，如果有一个不存在抛出异常
            resourceValidator.allExistByIdCollection(dto.getResourceIds());
            //4.2.验证资源必须是此角色的上级角色拥有的资源，如果没有上级角色则可以关联所有资源
            roleValidator.parentRoleAlreadyAssociateResources(role.getParentId(), dto.getResourceIds());

            //5.实例化角色资源实体列表，并入库
            List<RoleResourceRelation> roleResourceRelationList = roleResourceRelationWrapper
                    .instanceList(id, dto.getResourceIds());
            roleResourceRelationAutoService.saveBatch(roleResourceRelationList);
        } finally {
            //6.清除缓存
            Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME))
                    .ifPresent(Cache::clear);
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associatePermission(long id, RoleAssociatePermissionDto dto) {
        //1.数据验证
        //1.1.验证角色是否存在，如果不存在抛出异常
        Role role = roleValidator.existById(id);

        //2.删除角色权限关系实体
        rolePermissionRelationAutoService.remove(Wrappers.<RolePermissionRelation>lambdaQuery()
                .eq(RolePermissionRelation::getRoleId, id)
        );

        try {
            //3.如果传递的权限id集合为空，则什么都不用做，直接返回
            if (CollectionUtil.isEmpty(dto.getPermissionIds())) {
                return;
            }

            //4.再次进行数据验证
            //4.1.验证权限是否全部存在，如果有一个不存在抛出异常
            permissionValidator.allExistByIdCollection(dto.getPermissionIds());
            //4.2.验证权限必须是此角色的上级角色拥有的权限，如果没有上级角色则可以关联所有权限
            roleValidator.parentRoleAlreadyAssociatePermissions(role.getParentId(), dto.getPermissionIds());

            //5.实例化角色权限实体列表，并入库
            List<RolePermissionRelation> rolePermissionRelations = rolePermissionRelationWrapper
                    .instanceList(id, dto.getPermissionIds());
            rolePermissionRelationAutoService.saveBatch(rolePermissionRelations);
        } finally {
            //6.清除缓存
            Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME))
                    .ifPresent(Cache::clear);
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
        }
    }

    @Override
    @Cacheable(value = RbacCacheConstant.ROLE_TREE_LIST_CACHE_NAME, key = "#root.method.name"
            , unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result)")
    public List<Tree<Long>> treeList() {
        //1.查询所有角色实体
        List<Role> roles = roleAutoService.list();

        //2.包装数据
        return TreeUtil.build(roles, 0L, (role, node) -> {
            //2.1.id、上级id、排序、名称
            node.setId(role.getId()).setParentId(role.getParentId())
                    .setWeight(role.getCreateTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    .setName(role.getRoleName());
            //2.2.角色编码
            node.putExtra("roleCode", role.getRoleCode());
        });
    }
}
