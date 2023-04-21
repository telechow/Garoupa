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

import io.github.telechow.garoupa.api.entity.RolePermissionRelation;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 角色权限关系包装器
 *
 * @author Telechow
 * @since 2023/4/15 23:25
 */
@Component
public class RolePermissionRelationWrapper {

    /**
     * 根据 角色id、权限id实体集合 实例化 角色权限关系实体列表
     *
     * @param roleId                 角色id，非空
     * @param permissionIdCollection 权限id集合，非空
     * @return java.util.List<io.github.telechow.garoupa.api.entity.RoleResourceRelation> 角色权限关系实体列表
     * @author Telechow
     * @since 2023/4/15 22:49
     */
    public List<RolePermissionRelation> instanceList(@Nonnull Long roleId, Collection<Long> permissionIdCollection) {
        List<RolePermissionRelation> rolePermissionRelationList = new ArrayList<>(permissionIdCollection.size());
        for (Long permissionId : permissionIdCollection) {
            RolePermissionRelation rolePermissionRelation = new RolePermissionRelation();
            rolePermissionRelation.setRoleId(roleId).setPermissionId(permissionId);
            rolePermissionRelationList.add(rolePermissionRelation);
        }
        return rolePermissionRelationList;
    }
}
