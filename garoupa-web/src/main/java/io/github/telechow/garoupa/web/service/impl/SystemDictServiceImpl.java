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
package io.github.telechow.garoupa.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.constant.SystemDictCacheConstant;
import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.dict.CreateSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.PageSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.UpdateSystemDictDto;
import io.github.telechow.garoupa.api.entity.SystemDict;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo;
import io.github.telechow.garoupa.web.auto.service.ISystemDictAutoService;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import io.github.telechow.garoupa.web.service.ISystemDictService;
import io.github.telechow.garoupa.web.validator.SystemDictValidator;
import io.github.telechow.garoupa.web.wrapper.SystemDictWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统字典service实现
 *
 * @author Telechow
 * @since 2023/4/10 21:14
 */
@Service
@RequiredArgsConstructor
public class SystemDictServiceImpl implements ISystemDictService {

    private final SystemDictValidator systemDictValidator;

    private final SystemDictWrapper systemDictWrapper;

    private final ISystemDictAutoService systemDictAutoService;
    private final ISystemDictItemAutoService systemDictItemAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSystemDictDto dto) {
        //1.实例化系统字典实体，并入库
        SystemDict systemDict = systemDictWrapper.instance(dto);
        systemDictAutoService.save(systemDict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdateSystemDictDto dto) {
        //1.数据验证
        //1.1.根据 系统字典id 验证 系统字典 是否存在，如果不存在抛出异常
        SystemDict systemDict = systemDictValidator.existById(id);
        //1.2.验证系统字典是否被锁定，如果被锁定抛出异常
        systemDictValidator.isLocked(systemDict);

        //2.修改数据
        SystemDict updateSystemDict = new SystemDict();
        updateSystemDict.setId(id).setDictCode(dto.getDictCode()).setDictName(dto.getDictName())
                .setDictDescribe(dto.getDictDescribe()).setSortNumber(dto.getSortNumber());
        systemDictAutoService.updateById(updateSystemDict);

        //3.修改系统字典项
        systemDictItemAutoService.update(Wrappers.<SystemDictItem>lambdaUpdate()
                .set(SystemDictItem::getDictCode, dto.getDictCode())
                .set(SystemDictItem::getDictName, dto.getDictName())
                .eq(SystemDictItem::getDictId, id)
        );

        //4.清除缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.根据 系统字典id 验证 系统字典 是否存在，如果不存在抛出异常
        SystemDict systemDict = systemDictValidator.existById(id);
        //1.2.验证系统字典是否被锁定，如果被锁定抛出异常
        systemDictValidator.isLocked(systemDict);
        //1.3.验证系统字典中存在系统字典项，则不允许删除
        systemDictValidator.notAllowDeleteContainSystemDictItem(id);

        //2.逻辑删除数据
        systemDictAutoService.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setLock(long id, CommonSetLockDto dto) {
        //1.数据验证
        //1.1.根据 系统字典id 验证 系统字典 是否存在，如果不存在抛出异常
        systemDictValidator.existById(id);

        //2.修改实体，并入库
        SystemDict updateSystemDict = new SystemDict();
        updateSystemDict.setId(id).setLockSign(dto.getLockSign());
        systemDictAutoService.updateById(updateSystemDict);
    }

    @Override
    public Page<SystemDictPageVo> page(PageSystemDictDto dto) {
        //1.查询数据库
        Page<SystemDict> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<SystemDict> lambdaQueryWrapper = Wrappers.<SystemDict>lambdaQuery()
                //1.1.根据排序升序、字典编码升序排序
                .orderByAsc(SystemDict::getSortNumber)
                .orderByAsc(SystemDict::getDictCode);
        //1.2.设置字典编码模糊查询条件
        if (StrUtil.isNotBlank(dto.getDictCodeLike())) {
            lambdaQueryWrapper.apply("match(dict_code) against({0})", dto.getDictCodeLike());
        }
        //1.3.设置字典名称模糊查询条件
        if (StrUtil.isNotBlank(dto.getDictNameLike())) {
            lambdaQueryWrapper.apply("match(dict_name) against({0})", dto.getDictNameLike());
        }
        page = systemDictAutoService.page(page, lambdaQueryWrapper);

        //2.包装数据
        List<SystemDictPageVo> systemDictPageVoList = systemDictWrapper
                .systemDictCollectionToSystemDictPageVoList(page.getRecords());
        Page<SystemDictPageVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(systemDictPageVoList);
        return result;
    }
}
