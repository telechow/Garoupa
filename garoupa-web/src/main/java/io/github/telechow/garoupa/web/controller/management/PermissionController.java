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
import io.github.telechow.garoupa.api.dto.permission.CreatePermissionDto;
import io.github.telechow.garoupa.api.dto.permission.PagePermissionByMenuIdDto;
import io.github.telechow.garoupa.api.dto.permission.UpdatePermissionDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.permission.PermissionPageVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.IPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/m/permission")
@Tag(name = "权限-控制器", description = "权限-控制器")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    /**
     * 创建权限
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证权限编码的唯一性</li>
     * <li>需要验证菜单是否存在</li>
     *
     * @param dto 创建权限dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/14 15:24
     */
    @AuditLogAnnotation("创建权限")
    @PostMapping("/create")
    @Operation(summary = "创建权限"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证权限编码的唯一性</li>" +
            "<li>需要验证菜单是否存在</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:permission:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreatePermissionDto dto) {
        permissionService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改权限
     * <li>此接口要根据权限id开启分布式锁</li>
     * <li>需要保证权限编码的唯一性</li>
     * <li>不允许修改权限的菜单</li>
     *
     * @param id  权限id
     * @param dto 修改权限dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/14 15:41
     */
    @AuditLogAnnotation("修改权限")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改权限"
            , description = "<li>此接口要根据权限id开启分布式锁</li>" +
            "<li>需要保证权限编码的唯一性</li>" +
            "<li>不允许修改权限的菜单</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:permission:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdatePermissionDto dto) {
        permissionService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除权限
     * <li>此接口要根据权限id开启分布式锁</li>
     *
     * @param id 权限id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/14 15:50
     */
    @AuditLogAnnotation("逻辑删除权限")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除权限"
            , description = "<li>此接口要根据权限id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:permission:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        permissionService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 分页查询指定菜单中的权限
     * <li>按照创建时间升序排序</li>
     *
     * @param dto 根据菜单id分页查询权限dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.permission.PermissionPageVo>> 分页的权限分页vo
     * @author Telechow
     * @since 2023/4/14 16:02
     */
    @GetMapping("/page/by/menu")
    @Operation(summary = "分页查询指定菜单中的权限"
            , description = "<li>按照创建时间升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:permission:page-by-menu-id')")
    public ResponseResult<Page<PermissionPageVo>> pageByMenuId(@Validated PagePermissionByMenuIdDto dto) {
        return ResponseResult.data(permissionService.pageByMenuId(dto));
    }
}
