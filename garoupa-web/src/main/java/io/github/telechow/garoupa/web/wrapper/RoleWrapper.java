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

import io.github.telechow.garoupa.api.dto.role.CreateRoleDto;
import io.github.telechow.garoupa.api.entity.Role;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

/**
 * 角色包装器
 *
 * @author Telechow
 * @since 2023/4/15 20:08
 */
@Component
public class RoleWrapper {

    /**
     * 根据 创建角色dto 实例化 角色实体
     *
     * @param dto 创建角色dto
     * @return io.github.telechow.garoupa.api.entity.Role 角色实体
     * @author Telechow
     * @since 2023/4/15 20:09
     */
    public Role instance(@Nonnull CreateRoleDto dto) {
        Role role = new Role();
        role.setRoleCode(dto.getRoleCode()).setRoleName(dto.getRoleName()).setParentId(dto.getParentId());
        return role;
    }
}
