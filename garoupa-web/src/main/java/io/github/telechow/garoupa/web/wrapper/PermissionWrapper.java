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
package io.github.telechow.garoupa.web.wrapper;

import cn.hutool.core.collection.CollectionUtil;
import io.github.telechow.garoupa.api.dto.permission.CreatePermissionDto;
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.api.vo.permission.PermissionPageVo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 权限包装器
 *
 * @author Telechow
 * @since 2023/4/14 15:30
 */
@Component
public class PermissionWrapper {

    /**
     * 根据 创建权限dto 实例化 权限实体
     *
     * @param dto 创建权限dto
     * @return io.github.telechow.garoupa.api.entity.Permission 权限实体
     * @author Telechow
     * @since 2023/4/14 15:30
     */
    public Permission instance(@Nonnull CreatePermissionDto dto) {
        Permission permission = new Permission();
        permission.setPermissionCode(dto.getPermissionCode()).setPermissionName(dto.getPermissionName())
                .setResourceId(dto.getResourceId());
        return permission;
    }

    /**
     * 将 权限实体集合 包装成 权限分页vo列表
     *
     * @param permissionCollection 权限实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.permission.PermissionPageVo> 权限分页vo列表
     * @author Telechow
     * @since 2023/4/14 16:06
     */
    public List<PermissionPageVo> permissionCollectionToPermissionPageVoList(
            Collection<Permission> permissionCollection) {
        //1.如果权限实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(permissionCollection)) {
            return Collections.emptyList();
        }

        //2.包装数据
        List<PermissionPageVo> permissionPageVos = new ArrayList<>(permissionCollection.size());
        for (Permission permission : permissionCollection) {
            PermissionPageVo vo = new PermissionPageVo();
            vo.setId(permission.getId()).setPermissionCode(permission.getPermissionCode())
                    .setPermissionName(permission.getPermissionName());
            permissionPageVos.add(vo);
        }
        return permissionPageVos;
    }
}
