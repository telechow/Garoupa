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
package io.github.telechow.garoupa.api.constant;

/**
 * 系统参数缓存相关常量
 *
 * @author Telechow
 * @since 2023/4/7 23:33
 */
public class SystemParamCacheConstant {

    ///############################################################ 系统参数相关

    /**
     * 根据系统参数编码查询系统参数缓存键前缀
     */
    public static final String SYSTEM_PARAM_GET_BY_PARAM_CODE_CACHE_NAME = "SystemParam:GetByParamCode";

    ///############################################################ 系统参数分类相关

    /**
     * 根据系统参数id查询系统参数分类缓存键前缀
     */
    public static final String SYSTEM_PARAM_CATEGORY_GET_BY_ID_CACHE_NAME = "SystemParamCategory:GetById";
}