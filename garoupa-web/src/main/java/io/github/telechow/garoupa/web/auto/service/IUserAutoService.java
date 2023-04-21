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

import io.github.telechow.garoupa.api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author telechow
 * @since 2023-03-26
 */
public interface IUserAutoService extends IService<User> {

    /**
     * 根据用户id查询用户实体
     * <li color=red>此方法要加入缓存</li>
     *
     * @param id 用户id
     * @return io.github.telechow.garoupa.api.entity.User 用户实体
     * @author Telechow
     * @since 2023/4/3 20:18
     */
    User getUerByIdPutCache(Long id);
}
