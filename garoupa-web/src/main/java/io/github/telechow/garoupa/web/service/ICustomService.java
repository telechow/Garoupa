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
import io.github.telechow.garoupa.api.dto.user.PageUserDto;
import io.github.telechow.garoupa.api.dto.user.UserAssociateRoleDto;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;

/**
 * 客户service接口
 *
 * @author Telechow
 * @since 2023/4/17 16:19
 */
public interface ICustomService {

    /**
     * 逻辑删除客户
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>删除客户的同时，将会删除用户角色关系</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 客户id
     * @author Telechow
     * @since 2023/4/17 16:20
     */
    void logicDelete(long id);

    /**
     * 设置客户关联的角色
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>如果设置的角色列表为空，则表示清除客户的角色</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  客户id
     * @param dto 用户关联角色dto
     * @author Telechow
     * @since 2023/4/17 17:16
     */
    void associateRole(long id, UserAssociateRoleDto dto);

    /**
     * 分页查询客户
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询用户dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.user.UserPageVo> 分页的用户分页vo
     * @author Telechow
     * @since 2023/4/17 17:22
     */
    Page<UserPageVo> page(PageUserDto dto);
}
