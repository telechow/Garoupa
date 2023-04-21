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
package io.github.telechow.garoupa.web.service;

import cn.hutool.core.lang.tree.Tree;
import io.github.telechow.garoupa.api.dto.resource.CreateResourceDto;
import io.github.telechow.garoupa.api.dto.resource.UpdateResourceDto;

import java.util.List;

/**
 * 资源service接口
 *
 * @author Telechow
 * @since 2023/4/13 15:44
 */
public interface IResourceService {

    /**
     * 创建资源
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证资源编码的唯一性</li>
     * <li>需要验证上级资源是否存在，且为菜单</li>
     * <li>需要验证资源类型的值是否在系统字典中存在</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建资源dto
     * @author Telechow
     * @since 2023/4/13 16:18
     */
    void create(CreateResourceDto dto);

    /**
     * 修改资源
     * <li>此接口要根据资源id开启分布式锁</li>
     * <li>需要保证资源编码的唯一性</li>
     * <li>需要验证上级资源是否存在，且为菜单</li>
     * <li>不允许修改资源类型</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  资源id
     * @param dto 修改资源dto
     * @author Telechow
     * @since 2023/4/13 17:30
     */
    void update(long id, UpdateResourceDto dto);

    /**
     * 逻辑删除资源
     * <li>此接口要根据资源id开启分布式锁</li>
     * <li>如果资源包含下级资源，则不允许删除</li>
     * <li>如果资源关联了权限，则不允许删除</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 资源id
     * @author Telechow
     * @since 2023/4/13 18:02
     */
    void logicDelete(long id);

    /**
     * 查询资源树列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>
     *
     * @return java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>> 资源树列表
     * @author Telechow
     * @since 2023/4/13 21:35
     */
    List<Tree<Long>> treeList();

    /**
     * 查询菜单资源树列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>
     * <li>此接口只查询菜单，且不需要过多的字段，仅展示菜单的名称、顺序</li>
     *
     * @return java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>> 菜单资源树列表
     * @author Telechow
     * @since 2023/4/13 22:18
     */
    List<Tree<Long>> menuTreeList();
}
