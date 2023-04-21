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

import io.github.telechow.garoupa.api.constant.AuthenticationCacheConstant;
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.web.mapper.PermissionMapper;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author telechow
 * @since 2023-03-26
 */
@Service
@RequiredArgsConstructor
public class PermissionAutoServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionAutoService {

    @Override
    @Cacheable(value = AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME + "#2592000000"
            , key = "#userId", unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result)")
    public List<Permission> listPermissionByUserIdPutCache(Long userId) {
        return this.getBaseMapper().listPermissionByUserId(userId);
    }
}
