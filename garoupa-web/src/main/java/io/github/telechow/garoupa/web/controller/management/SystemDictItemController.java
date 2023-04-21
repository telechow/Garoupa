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
import io.github.telechow.garoupa.api.dto.common.CommonSetEffectiveStatusDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.CreateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.UpdateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.audit.log.annotation.AuditLogAnnotation;
import io.github.telechow.garoupa.web.service.ISystemDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统字典项表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/m/system/dict/item")
@Tag(name = "系统字典项-控制器", description = "系统字典项-控制器")
@RequiredArgsConstructor
public class SystemDictItemController {

    private final ISystemDictItemService systemDictItemService;

    /**
     * 创建系统字典项
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证同一个系统字典中的字典项值的唯一性</li>
     * <li>需要验证系统字典是否存在</li>
     *
     * @param dto 创建系统字典项dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/11 22:17
     */
    @AuditLogAnnotation("创建系统字典项")
    @PostMapping("/create")
    @Operation(summary = "创建系统字典项"
            , description = "<li>此接口要根据当前登录用户id开启分布式锁</li>" +
            "<li>需要保证同一个系统字典中的字典项值的唯一性</li>" +
            "<li>需要验证系统字典是否存在</li>")
    @Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:create')")
    public ResponseResult<Void> create(@Validated @RequestBody CreateSystemDictItemDto dto) {
        systemDictItemService.create(dto);
        return ResponseResult.ok();
    }

    /**
     * 修改系统字典项
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li>需要保证同一个系统字典中的字典项值的唯一性</li>
     * <li>不允许修改系统字典项的系统字典id</li>
     * <li>如果系统字典项是被锁定，则不允许修改</li>
     *
     * @param id  系统字典项id
     * @param dto 修改系统字典项dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/11 23:33
     */
    @AuditLogAnnotation("修改系统字典项")
    @PutMapping("/update/{id}")
    @Operation(summary = "修改系统字典项"
            , description = "<li>此接口要根据系统字典项id开启分布式锁</li>" +
            "<li>需要保证同一个系统字典中的字典项值的唯一性</li>" +
            "<li>不允许修改系统字典项的系统字典id</li>" +
            "<li>如果系统字典项是被锁定，则不允许修改</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:update')")
    public ResponseResult<Void> update(@PathVariable long id, @Validated @RequestBody UpdateSystemDictItemDto dto) {
        systemDictItemService.update(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 逻辑删除系统字典项
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li>如果系统字典项是被锁定，则不允许删除</li>
     *
     * @param id 系统字典项id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/11 23:48
     */
    @AuditLogAnnotation("逻辑删除系统字典项")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "逻辑删除系统字典项"
            , description = "<li>此接口要根据系统字典项id开启分布式锁</li>" +
            "<li>如果系统字典项是被锁定，则不允许删除</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:logic-delete')")
    public ResponseResult<Void> logicDelete(@PathVariable long id) {
        systemDictItemService.logicDelete(id);
        return ResponseResult.ok();
    }

    /**
     * 设置系统字典项的锁定标识
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     *
     * @param id  系统字典项id
     * @param dto 设置系统参数锁定标识dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/12 13:15
     */
    @AuditLogAnnotation("设置系统字典项的锁定标识")
    @PutMapping("/set/lock/{id}")
    @Operation(summary = "设置系统字典项的锁定标识"
            , description = "<li>此接口要根据系统字典项id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:set-lock')")
    public ResponseResult<Void> setLock(@PathVariable long id, @Validated @RequestBody SetSystemParamLockDto dto) {
        systemDictItemService.setLock(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 设置系统字典项的有效状态
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     *
     * @param id  系统字典项id
     * @param dto 通用设置有效状态dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.lang.Void> 响应结果对象
     * @author Telechow
     * @since 2023/4/12 13:49
     */
    @AuditLogAnnotation("设置系统字典项的有效状态")
    @PutMapping("/set/effective/status/{id}")
    @Operation(summary = "设置系统字典项的有效状态"
            , description = "<li>此接口要根据系统字典项id开启分布式锁</li>")
    @Lock4j(keys = "#id")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:set-effective-status')")
    public ResponseResult<Void> setEffectiveStatus(@PathVariable long id
            , @Validated @RequestBody CommonSetEffectiveStatusDto dto) {
        systemDictItemService.setEffectiveStatus(id, dto);
        return ResponseResult.ok();
    }

    /**
     * 根据系统字典id查询系统字典项列表
     * <li>按照排序升序、创建时间升序排序</li>
     *
     * @param systemDictId 系统字典id
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List < io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo>> 系统字典项分页vo列表
     * @author Telechow
     * @since 2023/4/12 15:30
     */
    @GetMapping("/list/by/system/dict/{systemDictId}")
    @Operation(summary = "根据系统字典id查询系统字典项列表"
            , description = "<li>按照排序升序、创建时间升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:list-by-system-dict')")
    public ResponseResult<List<SystemDictItemPageVo>> listBySystemDictId(@PathVariable long systemDictId) {
        return ResponseResult.data(systemDictItemService.listBySystemDictId(systemDictId));
    }

    /**
     * 根据系统字典编码查询系统字典项列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>此接口常用在下拉选择查询条件等位置</li>
     *
     * @param dictCode 系统字典编码
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<java.util.List<io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo>> 系统字典项vo列表
     * @author Telechow
     * @since 2023/4/12 16:22
     */
    @GetMapping("/list/by/system/dict/code/{dictCode}")
    @Operation(summary = "根据系统字典编码查询系统字典项列表"
            , description = "<li>按照排序升序、创建时间升序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('system-dict:dict-item:list-by-system-dict-code')")
    public ResponseResult<List<SystemDictItemVo>> listBySystemDictCode(@PathVariable String dictCode){
        return ResponseResult.data(systemDictItemService.listBySystemDictCode(dictCode));
    }
}
