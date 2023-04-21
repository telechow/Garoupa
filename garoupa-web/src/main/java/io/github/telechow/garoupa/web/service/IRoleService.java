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
import io.github.telechow.garoupa.api.dto.role.CreateRoleDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociatePermissionDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociateResourcesDto;
import io.github.telechow.garoupa.api.dto.role.UpdateRoleDto;

import java.util.List;

/**
 * 角色service接口
 *
 * @author Telechow
 * @since 2023/4/15 19:59
 */
public interface IRoleService {

    /**
     * 创建角色
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证角色编码、角色名称的唯一性</li>
     * <li>需要验证上级角色是否存在</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建角色dto
     * @author Telechow
     * @since 2023/4/15 19:59
     */
    void create(CreateRoleDto dto);

    /**
     * 修改角色
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>需要保证角色编码、角色名称的唯一性</li>
     * <li>不允许修改角色的上级角色</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  角色id
     * @param dto 修改角色dto
     * @author Telechow
     * @since 2023/4/15 20:16
     */
    void update(long id, UpdateRoleDto dto);

    /**
     * 逻辑删除角色
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>如果角色包含下级角色，则不允许删除</li>
     * <li>如果角色关联了用户，则不允许删除</li>
     * <li>删除角色的同时，要删除角色资源关系实体和角色权限关系实体</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 角色id
     * @author Telechow
     * @since 2023/4/15 20:28
     */
    void logicDelete(long id);

    /**
     * 给角色关联资源
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>被关联的资源必须全部存在</li>
     * <li>被关联的资源必须是此角色的上级角色拥有的资源，如果没有上级角色则可以关联所有资源</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  角色id
     * @param dto 角色关联资源dto
     * @author Telechow
     * @since 2023/4/15 22:36
     */
    void associateResources(long id, RoleAssociateResourcesDto dto);

    /**
     * 给角色关联权限
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>被关联的权限必须全部存在</li>
     * <li>被关联的权限必须是此角色的上级角色拥有的权限，如果没有上级角色则可以关联所有权限</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  角色id
     * @param dto 角色关联权限dto
     * @author Telechow
     * @since 2023/4/15 23:17
     */
    void associatePermission(long id, RoleAssociatePermissionDto dto);

    /**
     * 查询角色列表树
     * <li>按照创建时间升序排序</li>
     * <li>角色数据的量不会很多，所以我们直接同步查询出角色数据，构造成多棵树组成的列表</li>
     *
     * @return java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>> 角色树列表
     * @author Telechow
     * @since 2023/4/15 23:34
     */
    List<Tree<Long>> treeList();
}
