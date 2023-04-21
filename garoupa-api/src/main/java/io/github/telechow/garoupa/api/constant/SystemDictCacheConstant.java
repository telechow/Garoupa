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
 * 系统字典缓存相关常量
 *
 * @author Telechow
 * @since 2023/4/10 23:46
 */
public class SystemDictCacheConstant {

    ///############################################################ 系统字典缓存键

    ///############################################################ 系统字典项缓存键

    /**
     * 根据系统字典编码和系统字典项值获取系统字典项实体缓存前缀
     */
    public static final String SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME = "SystemDictItem:GetByDictCodeAndItemValue";

    /**
     * 根据系统字典编码获取系统字典项列表缓存前缀
     */
    public static final String SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME = "SystemDictItem:ListByDictCode";
}
