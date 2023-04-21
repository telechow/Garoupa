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
package io.github.telechow.garoupa.web.wrapper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import io.github.telechow.garoupa.api.dto.resource.CreateResourceDto;
import io.github.telechow.garoupa.api.entity.Resource;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.resource.ResourceTreeVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 资源包装器
 *
 * @author Telechow
 * @since 2023/4/13 16:30
 */
@Component
@RequiredArgsConstructor
public class ResourceWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 创建资源dto 实例化 资源实体
     *
     * @param dto 创建资源dto
     * @return io.github.telechow.garoupa.api.entity.Resource 资源实体
     * @author Telechow
     * @since 2023/4/13 16:31
     */
    public Resource instance(@Nonnull CreateResourceDto dto) {
        Resource resource = new Resource();
        resource.setParentId(dto.getParentId()).setResourceType(dto.getResourceType())
                .setResourceCode(dto.getResourceCode()).setResourceName(dto.getResourceName())
                .setIcon(dto.getIcon()).setRoute(dto.getRoute()).setSortNumber(dto.getSortNumber());
        return resource;
    }

    /**
     * 将 资源实体集合 包装成 资源树列表
     *
     * @param resourceCollection 资源实体集合
     * @return java.util.List<cn.hutool.core.lang.tree.Tree < java.lang.Long>> 资源树列表
     * @author Telechow
     * @since 2023/4/13 21:53
     */
    public List<Tree<Long>> resourceCollectionToTreeList(Collection<Resource> resourceCollection) {
        //1.如果资源实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(resourceCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        //2.1.资源类型系统字典数据
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.RBAC_RESOURCE_TYPE.getDictCode());

        //3.包装数据
        List<ResourceTreeVo> resourceTreeVos = new ArrayList<>(resourceCollection.size());
        for (Resource resource : resourceCollection) {
            ResourceTreeVo vo = new ResourceTreeVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(resource.getId()).setParentId(resource.getParentId()).setResourceType(resource.getResourceType())
                    .setResourceCode(resource.getResourceCode()).setResourceName(resource.getResourceName())
                    .setIcon(resource.getIcon()).setRoute(resource.getRoute()).setSortNumber(resource.getSortNumber());
            //3.2.包装资源类型名称
            Optional.ofNullable(vo.getResourceType())
                    .flatMap(rt -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(rt.toString(), sdi.getItemValue()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setResourceTypeName(sdi.getItemText()));
            resourceTreeVos.add(vo);
        }

        //4.将数据构造成树
        return TreeUtil.build(resourceTreeVos, 0L, (vo, node) -> {
            //4.1.id、上级id、排序、名称
            node.setId(vo.getId()).setParentId(vo.getParentId())
                    .setWeight(vo.getSortNumber()).setName(vo.getResourceName());
            //4.2.资源类型、资源类型名称
            node.putExtra("resourceType", vo.getResourceType());
            node.putExtra("resourceTypeName", vo.getResourceTypeName());
            //4.3.资源编码、资源名称、图标、路由
            node.putExtra("resourceCode", vo.getResourceCode());
            node.putExtra("resourceName", vo.getResourceName());
            node.putExtra("icon", vo.getIcon());
            node.putExtra("route", vo.getRoute());
        });
    }

    /**
     * 将 资源实体集合 包装成 菜单资源树列表
     *
     * @param resourceCollection 资源实体集合
     * @return java.util.List<cn.hutool.core.lang.tree.Tree < java.lang.Long>> 菜单资源树列表
     * @author Telechow
     * @since 2023/4/13 22:21
     */
    public List<Tree<Long>> resourceCollectionToMenuTreeList(Collection<Resource> resourceCollection) {
        //1.如果资源实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(resourceCollection)) {
            return Collections.emptyList();
        }

        //2.将数据构造成树
        return TreeUtil.build(resourceCollection.stream().toList(), 0L, (resource, node) -> {
            //2.1.id、上级id、排序、名称
            node.setId(resource.getId()).setParentId(resource.getParentId())
                    .setWeight(resource.getSortNumber()).setName(resource.getResourceName());
        });
    }
}
