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
 * rbac缓存相关常量
 *
 * @author Telechow
 * @since 2023/4/14 0:06
 */
public class RbacCacheConstant {

    /**
     * 查询资源树列表缓存名称
     */
    public static final String RESOURCE_TREE_LIST_CACHE_NAME = "Resource:TreeList";

    /**
     * 查询菜单资源树列表缓存名称
     */
    public static final String RESOURCE_MENU_TREE_LIST_CACHE_NAME = "Resource:MenuTreeList";

    /**
     * 根据菜单id分页查询权限缓存名称
     */
    public static final String PERMISSION_PAGE_BY_MENU_ID_CACHE_NAME = "Permission:PageByMenuId";

    /**
     * 查询角色树列表缓存名称
     */
    public static final String ROLE_TREE_LIST_CACHE_NAME = "Role:TreeList";
}
