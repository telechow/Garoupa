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
import io.github.telechow.garoupa.api.dto.user.PageUserDto;
import io.github.telechow.garoupa.api.dto.user.UserAssociateRoleDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.ICustomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 客户-控制器
 *
 * @author Telechow
 * @since 2023/4/17 13:00
 */
@RestController
@RequestMapping("/m/custom")
@Tag(name = "客户-控制器", description = "客户-控制器")
@RequiredArgsConstructor
public class CustomController {

    private final ICustomService customService;

    /**
     * 逻辑删除客户
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>删除客户的同时，将会删除用户角色关系</li>
     *
     * @param id 客户id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/17 16:20
     */
    @AuditLogAnnotation("逻辑删除客户")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除客户"
            , description = "<li>此接口要根据用户id开启分布式锁</li>" +
            "<li>删除客户的同时，将会删除用户角色关系</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:custom:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        customService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 设置客户关联的角色
     * <li>此接口要根据用户id开启分布式锁</li>
     * <li>如果设置的角色列表为空，则表示清除客户的角色</li>
     *
     * @param id  客户id
     * @param dto 用户关联角色dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/17 17:16
     */
    @AuditLogAnnotation("设置客户关联的角色")
    @PutMapping("/associate/role/{id}")
    @Operation(summary = "设置客户关联的角色"
            , description = "<li>此接口要根据用户id开启分布式锁</li>" +
            "<li>如果设置的角色列表为空，则表示清除客户的角色</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:custom:associate-role')")
    public ResponseResult<Void> associateRole(@PathVariable long id, @Validated @RequestBody UserAssociateRoleDto dto) {
        customService.associateRole(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 分页查询客户
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询用户dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.user.UserPageVo>> 分页的用户分页vo
     * @author Telechow
     * @since 2023/4/17 17:22
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询客户"
            , description = "<li>按照创建时间降序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('rbac:custom:page')")
    public ResponseResult<Page<UserPageVo>> page(@Validated PageUserDto dto) {
        return ResponseResult.data(customService.page(dto));
    }
}
