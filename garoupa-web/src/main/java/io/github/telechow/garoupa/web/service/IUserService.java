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
import io.github.telechow.garoupa.api.dto.user.*;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;

/**
 * 用户service接口
 *
 * @author Telechow
 * @since 2023/4/16 13:22
 */
public interface IUserService {

    /**
     * 创建用户
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证用户名的唯一性</li>
     * <li>创建的用户密码为默认密码，从系统参数中读取</li>
     * <li>创建的用户的用户类型是用户，而不是客户；表示管理端的用户</li>
     * <li>此接口不会给用户分配任何角色，需要调用其他接口完成</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建用户dto
     * @author Telechow
     * @since 2023/4/16 13:18
     */
    void create(CreateUserDto dto);

    /**
     * 逻辑删除用户
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>删除用户的同时，将会删除用户角色关系</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 用户id
     * @author Telechow
     * @since 2023/4/16 14:08
     */
    void logicDelete(long id);

    /**
     * 重置用户的密码
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>重置用户密码为默认密码，从系统参数中查询</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 用户id
     * @author Telechow
     * @since 2023/4/16 16:36
     */
    void resetPassword(long id);

    /**
     * 设置用户关联的角色
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>如果设置的角色列表为空，则表示清除用户的角色</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  用户id
     * @param dto 用户关联角色dto
     * @author Telechow
     * @since 2023/4/16 16:46
     */
    void associateRole(long id, UserAssociateRoleDto dto);

    /**
     * 分页查询用户
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询用户dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.user.UserPageVo> 分页的用户分页vo
     * @author Telechow
     * @since 2023/4/16 17:53
     */
    Page<UserPageVo> page(PageUserDto dto);

    /**
     * 当前登录用户修改自己的密码
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要输入原始密码和两次输入相同的新密码</li>
     * <li>新密码必须符合指定的正则表达式，从系统参数中查询</li>
     * <li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 用户修改自己的密码dto
     * @author Telechow
     * @since 2023/4/16 20:20
     */
    void updatePasswordSelf(UpdatePasswordSelfDto dto);

    /**
     * 当前登录用户修改自己的信息
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>除了昵称必填，其他信息都可以为空；昵称不做唯一性校验</li>
     * <li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 用户修改自己的信息dto
     * @author Telechow
     * @since 2023/4/17 16:30
     */
    void updateSelf(UpdateSelfDto dto);
}
