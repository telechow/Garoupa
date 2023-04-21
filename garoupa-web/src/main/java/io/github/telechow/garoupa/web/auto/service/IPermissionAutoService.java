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
package io.github.telechow.garoupa.web.auto.service;

import io.github.telechow.garoupa.api.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author telechow
 * @since 2023-03-26
 */
public interface IPermissionAutoService extends IService<Permission> {

    /**
     * 根据用户id查询用户拥有的权限实体列表
     * <li color=red>此方法要加入缓存</li>
     *
     * @param userId 用户id
     * @return java.util.List<io.github.telechow.garoupa.api.entity.Permission> 权限实体列表
     * @author Telechow
     * @since 2023/3/30 22:15
     */
    List<Permission> listPermissionByUserIdPutCache(Long userId);

}
