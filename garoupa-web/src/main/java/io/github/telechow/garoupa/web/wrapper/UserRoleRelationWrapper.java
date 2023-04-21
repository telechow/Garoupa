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

import io.github.telechow.garoupa.api.entity.UserRoleRelation;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户角色关系包装器
 *
 * @author Telechow
 * @since 2023/4/16 16:53
 */
@Component
public class UserRoleRelationWrapper {

    /**
     * 根据 用户id、角色id集合 实例化用户角色关系实体列表
     *
     * @param userId           用户id
     * @param roleIdCollection 角色id集合
     * @return java.util.List<io.github.telechow.garoupa.api.entity.UserRoleRelation> 实例化用户角色关系实体列表
     * @author Telechow
     * @since 2023/4/16 16:54
     */
    public List<UserRoleRelation> instanceList(@Nonnull Long userId, Collection<Long> roleIdCollection) {
        List<UserRoleRelation> userRoleRelations = new ArrayList<>(roleIdCollection.size());
        for (Long roleId : roleIdCollection) {
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUserId(userId).setRoleId(roleId);
            userRoleRelations.add(userRoleRelation);
        }
        return userRoleRelations;
    }
}
