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
 * 认证缓存常量
 *
 * @author Telechow
 * @since 2023/3/31 15:50
 */
public class AuthenticationCacheConstant {

    /**
     * 根据用户id查询用户拥有的权限实体列表缓存前缀
     */
    public static final String PERMISSION_LIST_BY_USER_ID_CACHE_NAME = "Permission:ListByUserId";

    /**
     * 根据用户id查询用户拥有的资源实体列表缓存前缀
     */
    public static final String RESOURCE_LIST_BY_USER_ID_CACHE_NAME = "Resource:ListByUserId";

    /**
     * 根据用户id查询用户实体缓存前缀
     */
    public static final String USER_GET_BY_ID_CACHE_NAME = "User:GetById";
}
