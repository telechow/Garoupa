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

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.AuthenticationCacheConstant;
import io.github.telechow.garoupa.api.constant.RbacCacheConstant;
import io.github.telechow.garoupa.api.dto.resource.CreateResourceDto;
import io.github.telechow.garoupa.api.dto.resource.UpdateResourceDto;
import io.github.telechow.garoupa.api.entity.Resource;
import io.github.telechow.garoupa.api.enums.rbac.resource.ResourceTypeEnum;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.web.auto.service.IResourceAutoService;
import io.github.telechow.garoupa.web.service.IResourceService;
import io.github.telechow.garoupa.web.validator.CommonValidator;
import io.github.telechow.garoupa.web.validator.ResourceValidator;
import io.github.telechow.garoupa.web.wrapper.ResourceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 资源service实现
 *
 * @author Telechow
 * @since 2023/4/13 15:44
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements IResourceService {

    private final ResourceValidator resourceValidator;
    private final CommonValidator commonValidator;

    private final ResourceWrapper resourceWrapper;

    private final IResourceAutoService resourceAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateResourceDto dto) {
        //1.数据验证
        //1.1.验证上级资源是否存在且为菜单，如果不满足条件抛出异常
        resourceValidator.parentExistAndIsMenuById(dto.getParentId());
        //1.2.验证资源类型的值是否在系统字典中存在，如果不存在抛出异常
        commonValidator.validateSystemDictItemValueExist(SystemDictEnum.RBAC_RESOURCE_TYPE, dto.getResourceType().toString());

        //2.实例化资源实体，并入库
        Resource resource = resourceWrapper.instance(dto);
        resourceAutoService.save(resource);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_MENU_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdateResourceDto dto) {
        //1.数据验证
        //1.1.验证资源是否存在，如果不存在抛出异常
        resourceValidator.existById(id);
        //1.2.验证上级资源是否存在且为菜单，如果不满足条件抛出异常
        resourceValidator.parentExistAndIsMenuById(dto.getParentId());

        //2.修改实体，并入库
        Resource updateResource = new Resource();
        updateResource.setId(id).setParentId(dto.getParentId()).setResourceCode(dto.getResourceCode())
                .setResourceName(dto.getResourceName()).setIcon(dto.getIcon()).setRoute(dto.getRoute())
                .setSortNumber(dto.getSortNumber());
        resourceAutoService.updateById(updateResource);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_MENU_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证资源是否存在，如果不存在抛出异常
        resourceValidator.existById(id);
        //1.2.验证资源是否包含下级资源，如果是则不允许删除
        resourceValidator.notAllowDeleteContainChildrenResource(id);
        //1.3.验证资源是否关联了权限，如果是则不允许删除
        resourceValidator.notAllowDeleteAssociationPermission(id);

        //2.逻辑删除数据
        resourceAutoService.removeById(id);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(RbacCacheConstant.RESOURCE_MENU_TREE_LIST_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Cacheable(value = RbacCacheConstant.RESOURCE_TREE_LIST_CACHE_NAME + "#2592000000"
            , key = "#root.method.name", unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result)")
    public List<Tree<Long>> treeList() {
        //1.查询所有资源实体
        List<Resource> resources = resourceAutoService.list();

        //2.包装数据
        return resourceWrapper.resourceCollectionToTreeList(resources);
    }

    @Override
    @Cacheable(value = RbacCacheConstant.RESOURCE_MENU_TREE_LIST_CACHE_NAME + "#2592000000"
            , key = "#root.method.name", unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result)")
    public List<Tree<Long>> menuTreeList() {
        //1.查询所有的菜单资源实体
        List<Resource> resources = resourceAutoService.list(Wrappers.<Resource>lambdaQuery()
                .eq(Resource::getResourceType, ResourceTypeEnum.MENU.getCode())
        );

        //2.包装数据
        return resourceWrapper.resourceCollectionToMenuTreeList(resources);
    }
}
