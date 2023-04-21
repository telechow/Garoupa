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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.SystemParamCacheConstant;
import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.web.mapper.SystemParamMapper;
import io.github.telechow.garoupa.web.auto.service.ISystemParamAutoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author telechow
 * @since 2023-04-06
 */
@Service
public class SystemParamAutoServiceImpl extends ServiceImpl<SystemParamMapper, SystemParam>
        implements ISystemParamAutoService {

    @Override
    @Cacheable(value = SystemParamCacheConstant.SYSTEM_PARAM_GET_BY_PARAM_CODE_CACHE_NAME + "#2592000000"
            , key = "#paramCode", unless = "#result == null")
    public SystemParam getSystemParamByParamCodePutCache(String paramCode) {
        return this.getBaseMapper().selectOne(Wrappers.<SystemParam>lambdaQuery()
                .eq(SystemParam::getParamCode, paramCode)
                .last("limit 1")
        );
    }
}
