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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.constant.AuthenticationCacheConstant;
import io.github.telechow.garoupa.api.constant.RbacCacheConstant;
import io.github.telechow.garoupa.api.dto.permission.CreatePermissionDto;
import io.github.telechow.garoupa.api.dto.permission.PagePermissionByMenuIdDto;
import io.github.telechow.garoupa.api.dto.permission.UpdatePermissionDto;
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.api.vo.permission.PermissionPageVo;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import io.github.telechow.garoupa.web.service.IPermissionService;
import io.github.telechow.garoupa.web.validator.PermissionValidator;
import io.github.telechow.garoupa.web.validator.ResourceValidator;
import io.github.telechow.garoupa.web.wrapper.PermissionWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 权限service实现
 *
 * @author Telechow
 * @since 2023/4/14 15:27
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements IPermissionService {

    private final ResourceValidator resourceValidator;
    private final PermissionValidator permissionValidator;

    private final PermissionWrapper permissionWrapper;

    private final IPermissionAutoService permissionAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreatePermissionDto dto) {
        //1.数据验证
        //1.1.验证菜单是否存在，如果不存在抛出异常
        resourceValidator.menuExistById(dto.getResourceId());

        //2.实例化权限实体，并入库
        Permission permission = permissionWrapper.instance(dto);
        permissionAutoService.save(permission);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.PERMISSION_PAGE_BY_MENU_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdatePermissionDto dto) {
        //1.数据验证
        //1.1.验证权限是否存在，如果不存在抛出异常
        permissionValidator.existById(id);

        //2.修改数据，并入库
        Permission updatePermission = new Permission();
        updatePermission.setId(id).setPermissionCode(dto.getPermissionCode()).setPermissionName(dto.getPermissionName());
        permissionAutoService.updateById(updatePermission);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.PERMISSION_PAGE_BY_MENU_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证权限是否存在，如果不存在抛出异常
        permissionValidator.existById(id);

        //2.逻辑删除数据
        permissionAutoService.removeById(id);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.PERMISSION_PAGE_BY_MENU_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Cacheable(value = RbacCacheConstant.PERMISSION_PAGE_BY_MENU_ID_CACHE_NAME
            , key = "#dto.menuId + ':' + #dto.current + ':' + #dto.size"
            , unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result.records)")
    public Page<PermissionPageVo> pageByMenuId(PagePermissionByMenuIdDto dto) {
        //1.查询数据
        Page<Permission> page = new Page<>(dto.getCurrent(), dto.getSize());
        page = permissionAutoService.page(page, Wrappers.<Permission>lambdaQuery()
                .select(Permission::getId, Permission::getPermissionCode, Permission::getPermissionName)
                .eq(Permission::getResourceId, dto.getMenuId())
                .orderByAsc(Permission::getCreateTime)
        );

        //2.包装数据
        List<PermissionPageVo> permissionPageVos = permissionWrapper
                .permissionCollectionToPermissionPageVoList(page.getRecords());
        Page<PermissionPageVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(permissionPageVos);
        return result;
    }
}
