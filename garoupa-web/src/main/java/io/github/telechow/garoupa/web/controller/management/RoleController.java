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

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.lock.annotation.Lock4j;
import io.github.telechow.garoupa.api.dto.role.CreateRoleDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociatePermissionDto;
import io.github.telechow.garoupa.api.dto.role.RoleAssociateResourcesDto;
import io.github.telechow.garoupa.api.dto.role.UpdateRoleDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/m/role")
@Tag(name = "角色-控制器", description = "角色-控制器")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    /**
     * 创建角色
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证角色编码、角色名称的唯一性</li>
     * <li>需要验证上级角色是否存在</li>
     *
     * @param dto 创建角色dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/15 19:59
     */
    @AuditLogAnnotation("创建角色")
    @PostMapping("/create")
    @Operation(summary = "创建角色"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证角色编码、角色名称的唯一性</li>" +
            "<li>需要验证上级角色是否存在</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateRoleDto dto) {
        roleService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改角色
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>需要保证角色编码、角色名称的唯一性</li>
     * <li>不允许修改角色的上级角色</li>
     *
     * @param id  角色id
     * @param dto 修改角色dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/15 20:16
     */
    @AuditLogAnnotation("修改角色")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改角色"
            , description = "<li>此接口要根据角色id开启分布式锁</li>" +
            "<li>需要保证角色编码、角色名称的唯一性</li>" +
            "<li>不允许修改角色的上级角色</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateRoleDto dto) {
        roleService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除角色
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>如果角色包含下级角色，则不允许删除</li>
     * <li>如果角色关联了用户，则不允许删除</li>
     * <li>删除角色的同时，要删除角色资源关系实体和角色权限关系实体</li>
     *
     * @param id 角色id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/15 20:28
     */
    @AuditLogAnnotation("逻辑删除角色")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除角色"
            , description = "<li>此接口要根据角色id开启分布式锁</li>" +
            "<li>如果角色包含下级角色，则不允许删除</li>" +
            "<li>如果角色关联了用户，则不允许删除</li>" +
            "<li>删除角色的同时，要删除角色资源关系实体和角色权限关系实体</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        roleService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 给角色关联资源
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>被关联的资源必须全部存在</li>
     * <li>被关联的资源必须是此角色的上级角色拥有的资源，如果没有上级角色则可以关联所有资源</li>
     *
     * @param id  角色id
     * @param dto 角色关联资源dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/15 22:36
     */
    @AuditLogAnnotation("给角色关联资源")
    @PutMapping("/associate/resources/{id}")
    @Operation(summary = "给角色关联资源"
            , description = "<li>此接口要根据角色id开启分布式锁</li>" +
            "<li>被关联的资源必须全部存在</li>" +
            "<li>被关联的资源必须是此角色的上级角色拥有的资源，如果没有上级角色则可以关联所有资源</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:associate-resources')")
    public ResponseResult<Void> associateResources(@PathVariable long id
            , @Validated @RequestBody RoleAssociateResourcesDto dto) {
        roleService.associateResources(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 给角色关联权限
     * <li>此接口要根据角色id开启分布式锁</li>
     * <li>被关联的权限必须全部存在</li>
     * <li>被关联的权限必须是此角色的上级角色拥有的权限，如果没有上级角色则可以关联所有权限</li>
     *
     * @param id  角色id
     * @param dto 角色关联权限dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/15 23:17
     */
    @AuditLogAnnotation("给角色关联权限")
    @PutMapping("/associate/permission/{id}")
    @Operation(summary = "给角色关联权限"
            , description = "<li>此接口要根据角色id开启分布式锁</li>" +
            "<li>被关联的权限必须全部存在</li>" +
            "<li>被关联的权限必须是此角色的上级角色拥有的权限，如果没有上级角色则可以关联所有权限</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:associate-permission')")
    public ResponseResult<Void> associatePermission(@PathVariable long id
            , @Validated @RequestBody RoleAssociatePermissionDto dto) {
        roleService.associatePermission(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 查询角色列表树
     * <li>按照创建时间升序排序</li>
     * <li>角色数据的量不会很多，所以我们直接同步查询出角色数据，构造成多棵树组成的列表</li>
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>>> 角色树列表
     * @author Telechow
     * @since 2023/4/15 23:34
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询角色列表树"
            , description = "<li>按照创建时间升序排序</li>" +
            "<li>角色数据的量不会很多，所以我们直接同步查询出角色数据，构造成多棵树组成的列表</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:role:tree-list')")
    public ResponseResult<List<Tree<Long>>> treeList() {
        return ResponseResult.data(roleService.treeList());
    }

}
