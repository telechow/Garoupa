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
import io.github.telechow.garoupa.api.dto.system.param.CreateSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.PageSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.dto.system.param.UpdateSystemParamDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.ISystemParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统参数表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/m/system/param")
@Tag(name = "系统参数-控制器", description = "系统参数-控制器")
@RequiredArgsConstructor
public class SystemParamController {

    private final ISystemParamService systemParamService;

    /**
     * 创建系统参数
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证参数编码、参数名称的唯一性</li>
     * <li>需要验证系统参数分类是否存在</li>
     *
     * @param dto 创建系统参数dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 19:37
     */
    @AuditLogAnnotation("创建系统参数")
    @PostMapping("/create")
    @Operation(summary = "创建系统参数"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证参数编码、参数名称的唯一性</li>" +
            "<li>需要验证系统参数分类是否存在</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:param:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateSystemParamDto dto) {
        systemParamService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改系统参数
     * <li>此接口要根据系统参数id开启分布式锁</li>
     * <li>需要保证参数编码、参数名称的唯一性</li>
     * <li>不允许修改系统参数的分类</li>
     * <li>如果系统参数是被锁定，则不允许修改</li>
     *
     * @param id  系统参数id
     * @param dto 修改系统参数dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 21:32
     */
    @AuditLogAnnotation("修改系统参数")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改系统参数"
            , description = "<li>此接口要根据系统参数id开启分布式锁</li>" +
            "<li>需要保证参数编码、参数名称的唯一性</li>" +
            "<li>不允许修改系统参数的分类</li>" +
            "<li>如果系统参数是被锁定，则不允许修改</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:param:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateSystemParamDto dto) {
        systemParamService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除系统参数
     * <li>此接口要根据系统参数id开启分布式锁</li>
     * <li>如果系统参数是被锁定，则不允许删除</li>
     *
     * @param id 系统参数id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/7 21:55
     */
    @AuditLogAnnotation("逻辑删除系统参数")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除系统参数"
            , description = "<li>此接口要根据系统参数id开启分布式锁</li>" +
            "<li>如果系统参数是被锁定，则不允许删除</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:param:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        systemParamService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 根据系统参数分类分页查询系统参数
     * <li>按照系统参数编码升序排序</li>
     *
     * @param dto 分页查询系统参数dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo>> 分页的系统参数分页vo
     * @author Telechow
     * @since 2023/4/7 22:30
     */
    @GetMapping("/page/by/category")
    @Operation(summary = "根据系统参数分类分页查询系统参数"
            , description = "<li>按照系统参数编码升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:param:page-by-category')")
    public ResponseResult<Page<SystemParamPageVo>> pageByCategory(@Validated PageSystemParamDto dto) {
        return ResponseResult.data(systemParamService.pageByCategory(dto));
    }

    /**
     * 设置系统参数的锁定标识
     * <li>此接口要根据系统参数id开启分布式锁</li>
     *
     * @param id  系统参数id
     * @param dto 设置系统参数锁定标识dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/8 20:23
     */
    @AuditLogAnnotation("设置系统参数的锁定标识")
    @PutMapping("/set/lock/{id}")
    @Operation(summary = "设置系统参数的锁定标识"
            , description = "<li>此接口要根据系统参数id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-param:param:set-lock')")
    public ResponseResult<Void> setLock(@PathVariable long id, @Validated @RequestBody SetSystemParamLockDto dto) {
        systemParamService.setLock(id, dto);
        return ResponseResult.ok();
    }
}
