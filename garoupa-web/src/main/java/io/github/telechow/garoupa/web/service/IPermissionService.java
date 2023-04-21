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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.permission.CreatePermissionDto;
import io.github.telechow.garoupa.api.dto.permission.PagePermissionByMenuIdDto;
import io.github.telechow.garoupa.api.dto.permission.UpdatePermissionDto;
import io.github.telechow.garoupa.api.vo.permission.PermissionPageVo;

/**
 * 权限service接口
 *
 * @author Telechow
 * @since 2023/4/14 15:27
 */
public interface IPermissionService {

    /**
     * 创建权限
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证权限编码的唯一性</li>
     * <li>需要验证菜单是否存在</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建权限dto
     * @author Telechow
     * @since 2023/4/14 15:24
     */
    void create(CreatePermissionDto dto);

    /**
     * 修改权限
     * <li>此接口要根据权限id开启分布式锁</li>
     * <li>需要保证权限编码的唯一性</li>
     * <li>不允许修改权限的菜单</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  权限id
     * @param dto 修改权限dto
     * @author Telechow
     * @since 2023/4/14 15:41
     */
    void update(long id, UpdatePermissionDto dto);

    /**
     * 逻辑删除权限
     * <li>此接口要根据权限id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 权限id
     * @author Telechow
     * @since 2023/4/14 15:50
     */
    void logicDelete(long id);

    /**
     * 分页查询指定菜单中的权限
     * <li>按照创建时间升序排序</li>
     *
     * @param dto    根据菜单id分页查询权限dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.permission.PermissionPageVo> 分页的权限分页vo
     * @author Telechow
     * @since 2023/4/14 16:02
     */
    Page<PermissionPageVo> pageByMenuId(PagePermissionByMenuIdDto dto);
}
