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
import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.dict.CreateSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.PageSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.UpdateSystemDictDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.ISystemDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统字典表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/m/system/dict")
@Tag(name = "系统字典-控制器", description = "系统字典-控制器")
@RequiredArgsConstructor
public class SystemDictController {

    private final ISystemDictService systemDictService;

    /**
     * 创建系统字典
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证字典编码的唯一性</li>
     *
     * @param dto 创建系统字典dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/10 21:31
     */
    @AuditLogAnnotation("创建系统字典")
    @PostMapping("/create")
    @Operation(summary = "创建系统字典"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证字典编码的唯一性</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateSystemDictDto dto) {
        systemDictService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改系统字典
     * <li>此接口要根据系统字典id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     * <li>如果系统分类被锁定，则不允许修改</li>
     *
     * @param id  系统字典id
     * @param dto 修改系统字典dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/10 23:00
     */
    @AuditLogAnnotation("修改系统字典")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改系统字典"
            , description = "<li>此接口要根据系统字典id开启分布式锁</li>" +
            "<li>需要保证参数分类名称的唯一性</li>" +
            "<li>如果系统分类被锁定，则不允许修改</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateSystemDictDto dto) {
        systemDictService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 删除系统字典
     * <li>此接口要根据系统字典id开启分布式锁</li>
     * <li>如果系统字典被锁定，则不允许删除</li>
     * <li>如果系统字典中存在系统字典项，则不允许删除</li>
     *
     * @param id 系统字典id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/11 15:47
     */
    @AuditLogAnnotation("删除系统字典")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除系统字典"
            , description = "<li>此接口要根据系统字典id开启分布式锁</li>" +
            "<li>如果系统字典被锁定，则不允许删除</li>" +
            "<li>如果系统字典中存在系统字典项，则不允许删除</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        systemDictService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 设置系统字典的锁定标识
     * <li>此接口要根据系统字典id开启分布式锁</li>
     *
     * @param id  系统字典id
     * @param dto 通用设置锁定dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/11 16:08
     */
    @AuditLogAnnotation("设置系统字典的锁定标识")
    @PutMapping("/set/lock/{id}")
    @Operation(summary = "设置系统字典的锁定标识"
            , description = "<li>此接口要根据系统字典id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict:set-lock')")
    public ResponseResult<Void> setLock(@PathVariable long id, @Validated @RequestBody CommonSetLockDto dto) {
        systemDictService.setLock(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 分页查询系统字典
     * <li>根据排序升序、字典编码升序排序</li>
     *
     * @param dto 分页查询系统字典dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo>> 分页的系统字典分页vo
     * @author Telechow
     * @since 2023/4/11 17:03
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询系统字典"
            , description = "<li>根据排序升序、字典编码升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict:page')")
    public ResponseResult<Page<SystemDictPageVo>> page(@Validated PageSystemDictDto dto) {
        return ResponseResult.data(systemDictService.page(dto));
    }

}
