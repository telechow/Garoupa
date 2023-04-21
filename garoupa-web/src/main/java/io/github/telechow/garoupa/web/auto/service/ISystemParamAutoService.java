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

import io.github.telechow.garoupa.api.entity.SystemParam;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author telechow
 * @since 2023-04-06
 */
public interface ISystemParamAutoService extends IService<SystemParam> {

    /**
     * 根据 系统参数编码 查询 系统参数实体，并写入缓存
     *
     * @param paramCode 系统参数编码
     * @return io.github.telechow.garoupa.api.entity.SystemParam 系统参数实体
     * @author Telechow
     * @since 2023/4/9 16:08
     */
    SystemParam getSystemParamByParamCodePutCache(String paramCode);
}
