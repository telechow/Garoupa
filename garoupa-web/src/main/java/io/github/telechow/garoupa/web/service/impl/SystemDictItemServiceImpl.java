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

import cn.hutool.core.text.StrPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.SystemDictCacheConstant;
import io.github.telechow.garoupa.api.dto.common.CommonSetEffectiveStatusDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.CreateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.UpdateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.entity.SystemDict;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import io.github.telechow.garoupa.web.service.ISystemDictItemService;
import io.github.telechow.garoupa.web.validator.CommonValidator;
import io.github.telechow.garoupa.web.validator.SystemDictItemValidator;
import io.github.telechow.garoupa.web.validator.SystemDictValidator;
import io.github.telechow.garoupa.web.wrapper.SystemDictItemWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统字典项service实现
 *
 * @author Telechow
 * @since 2023/4/11 22:19
 */
@Service
@RequiredArgsConstructor
public class SystemDictItemServiceImpl implements ISystemDictItemService {

    private final SystemDictValidator systemDictValidator;
    private final SystemDictItemValidator systemDictItemValidator;
    private final CommonValidator commonValidator;

    private final SystemDictItemWrapper systemDictItemWrapper;

    private final ISystemDictItemAutoService systemDictItemAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSystemDictItemDto dto) {
        //1.数据验证
        //1.1.验证系统字典是否存在，如果不存在抛出异常
        SystemDict systemDict = systemDictValidator.existById(dto.getDictId());

        //2.实例化系统字典项实体，并入库
        SystemDictItem systemDictItem = systemDictItemWrapper.instance(dto, systemDict);
        systemDictItemAutoService.save(systemDictItem);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(c -> {
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + false);
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + true);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdateSystemDictItemDto dto) {
        //1.数据验证
        //1.1.验证系统字典项是否存在，如果不存在抛出异常
        SystemDictItem systemDictItem = systemDictItemValidator.existById(id);
        //1.2.验证系统字典项是否被锁定，如果是抛出异常不允许修改
        systemDictItemValidator.isLocked(systemDictItem);

        //2.修改实体，并入库
        SystemDictItem updateSystemDictItem = new SystemDictItem();
        updateSystemDictItem.setId(id).setItemValue(dto.getItemValue()).setItemText(dto.getItemText())
                .setSortNumber(dto.getSortNumber());
        systemDictItemAutoService.updateById(updateSystemDictItem);

        //3.清除缓存
        //3.1.根据系统字典编码和系统字典项值获取系统字典项实体缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemDictItem.getDictCode() + StrPool.COLON + systemDictItem.getItemValue()));
        //3.2.根据系统字典编码获取系统字典项列表缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(c -> {
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + false);
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + true);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证系统字典项是否存在，如果不存在抛出异常
        SystemDictItem systemDictItem = systemDictItemValidator.existById(id);
        //1.2.验证系统字典项是否被锁定，如果是抛出异常不允许删除
        systemDictItemValidator.isLocked(systemDictItem);

        //2.逻辑删除系统字典项
        systemDictItemAutoService.removeById(id);

        //3.清除缓存
        //3.1.根据系统字典编码和系统字典项值获取系统字典项实体缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemDictItem.getDictCode() + StrPool.COLON + systemDictItem.getItemValue()));
        //3.2.根据系统字典编码获取系统字典项列表缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(c -> {
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + false);
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + true);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setLock(long id, SetSystemParamLockDto dto) {
        //1.数据验证
        //1.1.验证系统字典项是否存在，如果不存在抛出异常
        SystemDictItem systemDictItem = systemDictItemValidator.existById(id);

        //2.修改实体，并入库
        SystemDictItem updateSystemDictItem = new SystemDictItem();
        updateSystemDictItem.setId(id).setLockSign(dto.getLockSign());
        systemDictItemAutoService.updateById(updateSystemDictItem);

        //3.清除缓存
        //3.1.根据系统字典编码和系统字典项值获取系统字典项实体缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemDictItem.getDictCode() + StrPool.COLON + systemDictItem.getItemValue()));
        //3.2.根据系统字典编码获取系统字典项列表缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(c -> {
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + false);
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + true);
                });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setEffectiveStatus(long id, CommonSetEffectiveStatusDto dto) {
        //1.数据验证
        //1.1.验证系统字典项是否存在，如果不存在抛出异常
        SystemDictItem systemDictItem = systemDictItemValidator.existById(id);
        //1.2.验证有效状态的值是否存在，如果不存在抛出异常
        commonValidator.validateSystemDictItemValueExist(SystemDictEnum.EFFECTIVE_STATUS, dto.getEffectiveStatus().toString());

        //2.修改实体，并入库
        SystemDictItem updateSystemDictItem = new SystemDictItem();
        updateSystemDictItem.setId(id).setEffectiveStatus(dto.getEffectiveStatus());
        systemDictItemAutoService.updateById(updateSystemDictItem);

        //3.清除缓存
        //3.1.根据系统字典编码和系统字典项值获取系统字典项实体缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemDictItem.getDictCode() + StrPool.COLON + systemDictItem.getItemValue()));
        //3.2.根据系统字典编码获取系统字典项列表缓存
        Optional.ofNullable(cacheManager.getCache(SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME))
                .ifPresent(c -> {
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + false);
                    c.evict(systemDictItem.getDictCode() + StrPool.COLON + true);
                });
    }

    @Override
    public List<SystemDictItemPageVo> listBySystemDictId(long systemDictId) {
        //1.查询数据
        List<SystemDictItem> systemDictItems = systemDictItemAutoService.list(Wrappers.<SystemDictItem>lambdaQuery()
                .eq(SystemDictItem::getDictId, systemDictId)
                .orderByAsc(SystemDictItem::getSortNumber)
                .orderByAsc(SystemDictItem::getCreateTime)
        );

        //2.包装数据
        return systemDictItemWrapper.systemDictItemCollectionToSystemDictItemPageVoList(systemDictItems);
    }

    @Override
    public List<SystemDictItemVo> listBySystemDictCode(String dictCode) {
        return systemDictItemAutoService.listSystemDictItemByDictCodePutCache(dictCode, true);
    }
}
