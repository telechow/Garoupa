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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.SystemParamCacheConstant;
import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.param.category.CreateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.dto.system.param.category.UpdateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.entity.SystemParamCategory;
import io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo;
import io.github.telechow.garoupa.web.auto.service.ISystemParamCategoryAutoService;
import io.github.telechow.garoupa.web.service.ISystemParamCategory;
import io.github.telechow.garoupa.web.validator.SystemParamCategoryValidator;
import io.github.telechow.garoupa.web.wrapper.SystemParamCategoryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统参数分类service实现
 *
 * @author Telechow
 * @since 2023/4/6 17:41
 */
@Service
@RequiredArgsConstructor
public class SystemParamCategoryImpl implements ISystemParamCategory {

    private final SystemParamCategoryValidator systemParamCategoryValidator;

    private final SystemParamCategoryWrapper systemParamCategoryWrapper;

    private final ISystemParamCategoryAutoService systemParamCategoryAutoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSystemParamCategoryDto dto) {
        //1.实例化系统参数分类实体，并入库
        SystemParamCategory systemParamCategory = systemParamCategoryWrapper.instance(dto);
        systemParamCategoryAutoService.save(systemParamCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = SystemParamCacheConstant.SYSTEM_PARAM_CATEGORY_GET_BY_ID_CACHE_NAME + "#2592000000", key = "#id")
    public void update(long id, UpdateSystemParamCategoryDto dto) {
        //1.数据验证
        //1.1.根据 系统参数分类id 验证 系统参数分类实体 是否存在，如果不存在抛出异常
        SystemParamCategory systemParamCategory = systemParamCategoryValidator.existById(id);
        //1.2.验证系统参数分类是否被锁定，如果被锁定抛出异常
        systemParamCategoryValidator.isLocked(systemParamCategory);

        //2.修改数据
        SystemParamCategory updateSystemParamCategory = new SystemParamCategory();
        updateSystemParamCategory.setId(id).setCategoryName(dto.getCategoryName());
        systemParamCategoryAutoService.updateById(updateSystemParamCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = SystemParamCacheConstant.SYSTEM_PARAM_CATEGORY_GET_BY_ID_CACHE_NAME + "#2592000000", key = "#id")
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.根据 系统参数分类id 验证 系统参数分类实体 是否存在，如果不存在抛出异常
        SystemParamCategory systemParamCategory = systemParamCategoryValidator.existById(id);
        //1.2.验证系统参数分类是否被锁定，如果被锁定抛出异常
        systemParamCategoryValidator.isLocked(systemParamCategory);
        //1.3.验证系统参数分类中存在系统参数，则不允许删除
        systemParamCategoryValidator.notAllowDeleteContainSystemParam(id);

        //2.逻辑删除数据
        systemParamCategoryAutoService.removeById(id);
    }

    @Override
    public List<SystemParamCategoryVo> list() {
        //1.查询数据库
        List<SystemParamCategory> systemParamCategories = systemParamCategoryAutoService.list(
                Wrappers.<SystemParamCategory>lambdaQuery()
                        .select(SystemParamCategory::getId, SystemParamCategory::getCategoryName
                                , SystemParamCategory::getLockSign)
                        .orderByAsc(SystemParamCategory::getCreateTime)
        );

        //2.包装数据
        return systemParamCategoryWrapper.systemParamCategoryCollectionToSystemParamCategoryVoList(systemParamCategories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = SystemParamCacheConstant.SYSTEM_PARAM_CATEGORY_GET_BY_ID_CACHE_NAME + "#2592000000", key = "#id")
    public void setLock(long id, CommonSetLockDto dto) {
        //1.数据验证
        //1.1.根据 系统参数分类id 验证 系统参数分类实体 是否存在，如果不存在抛出异常
        systemParamCategoryValidator.existById(id);

        //2.修改实体，并入库
        SystemParamCategory updateSystemParamCategory = new SystemParamCategory();
        updateSystemParamCategory.setId(id).setLockSign(dto.getLockSign());
        systemParamCategoryAutoService.updateById(updateSystemParamCategory);
    }
}
