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
import io.github.telechow.garoupa.api.constant.SystemParamCacheConstant;
import io.github.telechow.garoupa.api.dto.system.param.CreateSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.PageSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.dto.system.param.UpdateSystemParamDto;
import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo;
import io.github.telechow.garoupa.web.auto.service.ISystemParamAutoService;
import io.github.telechow.garoupa.web.service.ISystemParamService;
import io.github.telechow.garoupa.web.validator.SystemParamCategoryValidator;
import io.github.telechow.garoupa.web.validator.SystemParamValidator;
import io.github.telechow.garoupa.web.wrapper.SystemParamWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统参数service实现
 *
 * @author Telechow
 * @since 2023/4/6 15:09
 */
@Service
@RequiredArgsConstructor
public class SystemParamServiceImpl implements ISystemParamService {

    private final SystemParamCategoryValidator systemParamCategoryValidator;
    private final SystemParamValidator systemParamValidator;

    private final SystemParamWrapper systemParamWrapper;

    private final ISystemParamAutoService systemParamAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateSystemParamDto dto) {
        //1.数据验证
        //1.1.验证系统参数分类是否存在，如果不存在抛出异常
        systemParamCategoryValidator.existById(dto.getParamCategoryId());

        //2.实例化系统参数实体，并入库
        SystemParam systemParam = systemParamWrapper.instance(dto);
        systemParamAutoService.save(systemParam);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(long id, UpdateSystemParamDto dto) {
        //1.数据验证
        //1.1.验证系统参数是否存在，如果不存在抛出异常
        SystemParam systemParam = systemParamValidator.existById(id);
        //1.2.验证系统参数是否是否被锁定，如果是抛出异常不允许修改
        systemParamValidator.isLocked(systemParam);

        //2.修改实体，并入库
        SystemParam updateSystemParam = new SystemParam();
        updateSystemParam.setId(id).setParamCode(dto.getParamCode()).setParamName(dto.getParamName())
                .setParamValue(dto.getParamValue());
        systemParamAutoService.updateById(updateSystemParam);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(SystemParamCacheConstant.SYSTEM_PARAM_GET_BY_PARAM_CODE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemParam.getParamCode()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证系统参数是否存在，如果不存在抛出异常
        SystemParam systemParam = systemParamValidator.existById(id);
        //1.2.验证系统参数是否是否被锁定，如果是抛出异常不允许删除
        systemParamValidator.isLocked(systemParam);

        //2.逻辑删除系统参数
        systemParamAutoService.removeById(id);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(SystemParamCacheConstant.SYSTEM_PARAM_GET_BY_PARAM_CODE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemParam.getParamCode()));
    }

    @Override
    public Page<SystemParamPageVo> pageByCategory(PageSystemParamDto dto) {
        //1.查询数据
        Page<SystemParam> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<SystemParam> lambdaQueryWrapper = Wrappers.<SystemParam>lambdaQuery()
                .eq(SystemParam::getParamCategoryId, dto.getParamCategoryId())
                //1.1.按照系统参数编码升序排序
                .orderByAsc(SystemParam::getParamCode);
        //1.2.设置参数编码模糊查询条件、参数名称模糊查询条件
        if (StrUtil.isNotBlank(dto.getParamCodeLike())) {
            lambdaQueryWrapper.apply("match(param_code) against({0})", dto.getParamCodeLike());
        }
        if (StrUtil.isNotBlank(dto.getParamNameLike())) {
            lambdaQueryWrapper.apply("match(param_name) against({0})", dto.getParamNameLike());
        }
        page = systemParamAutoService.page(page, lambdaQueryWrapper);

        //2.包装数据
        List<SystemParamPageVo> systemParamPageVoList = systemParamWrapper
                .systemParamCollectionToSystemParamPageVoList(page.getRecords());
        Page<SystemParamPageVo> result = new Page<>(dto.getCurrent(), dto.getSize(), page.getTotal());
        result.setRecords(systemParamPageVoList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setLock(long id, SetSystemParamLockDto dto) {
        //1.数据验证
        //1.1.验证系统参数是否存在，如果不存在抛出异常
        SystemParam systemParam = systemParamValidator.existById(id);

        //2.修改实体，并入库
        SystemParam updateSystemParam = new SystemParam();
        updateSystemParam.setId(id).setLockSign(dto.getLockSign());
        systemParamAutoService.updateById(updateSystemParam);

        //3.清除缓存
        Optional.ofNullable(cacheManager.getCache(SystemParamCacheConstant.SYSTEM_PARAM_GET_BY_PARAM_CODE_CACHE_NAME))
                .ifPresent(c -> c.evict(systemParam.getParamCode()));
    }
}
