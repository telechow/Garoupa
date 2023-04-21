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
package io.github.telechow.garoupa.web.controller.management;

import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.user.*;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/m/user")
@Tag(name = "用户-控制器", description = "用户-控制器")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 创建用户
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证用户名的唯一性</li>
     * <li>创建的用户密码为默认密码，从系统参数中读取</li>
     * <li>创建的用户的用户类型是用户，而不是客户；表示管理端的用户</li>
     * <li>此接口不会给用户分配任何角色，需要调用其他接口完成</li>
     *
     * @param dto 创建用户dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/16 13:18
     */
    @AuditLogAnnotation("创建用户")
    @PostMapping("/create")
    @Operation(summary = "创建用户"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证用户名的唯一性</li>" +
            "<li>创建的用户的用户类型是用户，而不是客户；表示管理端的用户</li>" +
            "<li>创建的用户密码为默认密码，从系统参数中读取</li>" +
            "<li>此接口不会给用户分配任何角色，需要调用其他接口完成</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:user:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateUserDto dto) {
        userService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除用户
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>删除用户的同时，将会删除用户角色关系</li>
     *
     * @param id 用户id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/16 14:08
     */
    @AuditLogAnnotation("逻辑删除用户")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除用户"
            , description = "<li>此接口要根据用户id开启分布式锁</li>" +
            "<li>删除用户的同时，将会删除用户角色关系</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:user:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        userService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 重置用户的密码
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>重置用户密码为默认密码，从系统参数中查询</li>
     *
     * @param id 用户id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/16 16:36
     */
    @AuditLogAnnotation("重置用户的密码")
    @PutMapping("/reset/password/{id}")
    @Operation(summary = "重置用户的密码"
            , description = "<li>此接口要根据用户id开启分布式锁</li>" +
            "<li>重置用户密码为默认密码，从系统参数中查询</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:user:reset-password')")
    public ResponseResult<Void> resetPassword(@PathVariable long id) {
        userService.resetPassword(id);
        return ResponseResult.ok();
    }

    /**
     * 设置用户关联的角色
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>如果设置的角色列表为空，则表示清除用户的角色</li>
     *
     * @param id  用户id
     * @param dto 用户关联角色dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/16 16:46
     */
    @AuditLogAnnotation("设置用户关联的角色")
    @PutMapping("/associate/role/{id}")
    @Operation(summary = "设置用户关联的角色"
            , description = "<li>此接口要根据用户id开启分布式锁</li>" +
            "<li>如果设置的角色列表为空，则表示清除用户的角色</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:user:associate-role')")
    public ResponseResult<Void> associateRole(@PathVariable long id, @Validated @RequestBody UserAssociateRoleDto dto) {
        userService.associateRole(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 分页查询用户
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询用户dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.user.UserPageVo>> 分页的用户分页vo
     * @author Telechow
     * @since 2023/4/16 17:53
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询用户"
            , description = "<li>按照创建时间降序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:user:page')")
    public ResponseResult<Page<UserPageVo>> page(@Validated PageUserDto dto) {
        return ResponseResult.data(userService.page(dto));
    }

    /**
     * 当前登录用户修改自己的密码
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要输入原始密码和两次输入相同的新密码</li>
     * <li>新密码必须符合指定的正则表达式，从系统参数中查询</li>
     * <li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>
     *
     * @param dto 用户修改自己的密码dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void>
     * @author Telechow
     * @since 2023/4/16 20:20
     */
    @AuditLogAnnotation("当前登录用户修改自己的密码")
    @PutMapping("/update/password/self")
    @Operation(summary = "当前登录用户修改自己的密码"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要输入原始密码和两次输入相同的新密码</li>" +
            "<li>新密码必须符合指定的正则表达式，从系统参数中查询</li>" +
            "<li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    public ResponseResult<Void> updatePasswordSelf(@Validated @RequestBody UpdatePasswordSelfDto dto) {
        userService.updatePasswordSelf(dto);
        return ResponseResult.ok();
    }

    /**
     * 当前登录用户修改自己的信息
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>除了昵称必填，其他信息都可以为空；昵称不做唯一性校验</li>
     * <li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>
     *
     * @param dto 用户修改自己的信息dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/17 16:30
     */
    @AuditLogAnnotation("当前登录用户修改自己的信息")
    @PutMapping("/update/self")
    @Operation(summary = "当前登录用户修改自己的信息"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>除了昵称必填，其他信息都可以为空；昵称不做唯一性校验</li>" +
            "<li>此接口认证成功后即可访问，但只允许用户访问，不允许客户访问</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    public ResponseResult<Void> updateSelf(@Validated @RequestBody UpdateSelfDto dto) {
        userService.updateSelf(dto);
        return ResponseResult.ok();
    }

}
