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
package io.github.telechow.garoupa.web.auto.service.impl;

import io.github.telechow.garoupa.api.constant.SystemParamCacheConstant;
import io.github.telechow.garoupa.api.entity.SystemParamCategory;
import io.github.telechow.garoupa.web.mapper.SystemParamCategoryMapper;
import io.github.telechow.garoupa.web.auto.service.ISystemParamCategoryAutoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数分类表 服务实现类
 * </p>
 *
 * @author telechow
 * @since 2023-04-06
 */
@Service
public class SystemParamCategoryAutoServiceImpl extends ServiceImpl<SystemParamCategoryMapper, SystemParamCategory>
        implements ISystemParamCategoryAutoService {

    @Override
    @Cacheable(value = SystemParamCacheConstant.SYSTEM_PARAM_CATEGORY_GET_BY_ID_CACHE_NAME + "#2592000000"
            , key = "#id", unless = "#result == null")
    public SystemParamCategory getByIdPutCache(Long id) {
        return this.getBaseMapper().selectById(id);
    }
}
