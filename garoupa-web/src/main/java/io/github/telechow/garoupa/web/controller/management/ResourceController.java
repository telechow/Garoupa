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
import io.github.telechow.garoupa.api.dto.resource.CreateResourceDto;
import io.github.telechow.garoupa.api.dto.resource.UpdateResourceDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.IResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 资源（菜单或按钮）表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-03-25
 */
@RestController
@RequestMapping("/m/resource")
@Tag(name = "资源-控制器", description = "资源-控制器")
@RequiredArgsConstructor
public class ResourceController {

    private final IResourceService resourceService;

    /**
     * 创建资源
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证资源编码的唯一性</li>
     * <li>需要验证上级资源是否存在，且为菜单</li>
     * <li>需要验证资源类型的值是否在系统字典中存在</li>
     *
     * @param dto 创建资源dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/13 16:18
     */
    @AuditLogAnnotation("创建资源")
    @PostMapping("/create")
    @Operation(summary = "创建资源"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证资源编码的唯一性</li>" +
            "<li>需要验证上级资源是否存在，且为菜单</li>" +
            "<li>需要验证资源类型的值是否在系统字典中存在</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:resource:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateResourceDto dto) {
        resourceService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改资源
     * <li>此接口要根据资源id开启分布式锁</li>
     * <li>需要保证资源编码的唯一性</li>
     * <li>需要验证上级资源是否存在，且为菜单</li>
     * <li>不允许修改资源类型</li>
     *
     * @param id  资源id
     * @param dto 修改资源dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/13 17:30
     */
    @AuditLogAnnotation("修改资源")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改资源"
            , description = "<li>此接口要根据资源id开启分布式锁</li>" +
            "<li>需要保证资源编码的唯一性</li>" +
            "<li>需要验证上级资源是否存在，且为菜单</li>" +
            "<li>不允许修改资源类型</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:resource:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateResourceDto dto) {
        resourceService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除资源
     * <li>此接口要根据资源id开启分布式锁</li>
     * <li>如果资源包含下级资源，则不允许删除</li>
     * <li>如果资源关联了权限，则不允许删除</li>
     *
     * @param id 资源id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/13 18:02
     */
    @AuditLogAnnotation("逻辑删除资源")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除资源"
            , description = "<li>此接口要根据资源id开启分布式锁</li>" +
            "<li>如果资源包含下级资源，则不允许删除</li>" +
            "<li>如果资源关联了权限，则不允许删除</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:resource:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        resourceService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 查询资源树列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>
     * <li>此接口主要用户资源的CRUD的界面，所以树结构中展示详细的信息，且按钮也一并查询</li>
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>>> 资源树列表
     * @author Telechow
     * @since 2023/4/13 21:35
     */
    @GetMapping("/tree/list")
    @Operation(summary = "查询资源树列表"
            , description = "<li>按照排序升序、创建时间升序排序</li>" +
            "<li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>" +
            "<li>此接口主要用户资源的CRUD的界面，所以树结构中展示详细的信息，且按钮也一并查询</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:resource:tree-list')")
    public ResponseResult<List<Tree<Long>>> treeList() {
        return ResponseResult.data(resourceService.treeList());
    }

    /**
     * 查询菜单资源树列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>
     * <li>此接口只查询菜单，且不需要过多的字段，仅展示菜单的名称、顺序</li>
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List < cn.hutool.core.lang.tree.Tree < java.lang.Long>>> 菜单资源树列表
     * @author Telechow
     * @since 2023/4/13 22:18
     */
    @GetMapping("/menu/tree/list")
    @Operation(summary = "查询菜单资源树列表"
            , description = "<li>按照排序升序、创建时间升序排序</li>" +
            "<li>资源数据的量不会很多，所以我们直接同步查询出资源数据，构造成多棵树组成的列表</li>" +
            "<li>此接口只查询菜单，且不需要过多的字段，仅展示菜单的名称、顺序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:resource:menu-tree-list')")
    public ResponseResult<List<Tree<Long>>> menuTreeList() {
        return ResponseResult.data(resourceService.menuTreeList());
    }
}
