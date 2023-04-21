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
import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.param.category.CreateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.dto.system.param.category.UpdateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.ISystemParamCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统参数分类表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/m/system/param/category")
@Tag(name = "系统参数分类-控制器", description = "系统参数分类-控制器")
@RequiredArgsConstructor
public class SystemParamCategoryController {

    private final ISystemParamCategory systemParamCategory;

    /**
     * 创建系统参数分类
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     *
     * @param dto 创建系统参数分类dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/6 16:32
     */
    @AuditLogAnnotation("创建系统参数分类")
    @PostMapping("/create")
    @Operation(summary = "创建系统参数分类"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证参数分类名称的唯一性</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:category:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateSystemParamCategoryDto dto) {
        systemParamCategory.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改系统参数分类
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     * <li>如果系统参数分类被锁定，则不允许修改</li>
     *
     * @param id  系统参数分类id
     * @param dto 修改系统参数分类dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 14:39
     */
    @AuditLogAnnotation("修改系统参数分类")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改系统参数分类"
            , description = "<li>此接口要根据系统参数分类id开启分布式锁</li>" +
            "<li>需要保证参数分类名称的唯一性</li>" +
            "<li>如果系统参数分类被锁定，则不允许修改</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:category:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateSystemParamCategoryDto dto) {
        systemParamCategory.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 删除系统参数分类
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     * <li>如果系统参数分类被锁定，则不允许删除</li>
     * <li>如果系统参数分类中存在系统参数，则不允许删除</li>
     *
     * @param id 系统参数分类id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 16:23
     */
    @AuditLogAnnotation("删除系统参数分类")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除系统参数分类"
            , description = "<li>此接口要根据系统参数分类id开启分布式锁</li>" +
            "<li>如果系统参数分类被锁定，则不允许删除</li>" +
            "<li>如果系统参数分类中存在系统参数，则不允许删除</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:category:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        systemParamCategory.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 查询系统参数分类列表
     * <li>根据创建时间升序排序</li>
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List < io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo>> 系统参数分类vo列表
     * @author Telechow
     * @since 2023/4/7 16:48
     */
    @GetMapping("/list")
    @Operation(summary = "查询系统参数分类列表"
            , description = "<li>根据创建时间升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:category:list')")
    public ResponseResult<List<SystemParamCategoryVo>> list() {
        return ResponseResult.data(systemParamCategory.list());
    }

    /**
     * 设置系统参数分类的锁定标识
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     *
     * @param id  系统参数分类id
     * @param dto 通用设置锁定dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/8 22:49
     */
    @AuditLogAnnotation("设置系统参数分类的锁定标识")
    @PutMapping("/set/lock/{id}")
    @Operation(summary = "设置系统参数分类的锁定标识"
            , description = "<li>此接口要根据系统参数分类id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:category:set-lock')")
    public ResponseResult<Void> setLock(@PathVariable long id, @Validated @RequestBody CommonSetLockDto dto) {
        systemParamCategory.setLock(id, dto);
        return ResponseResult.ok();
    }
}
