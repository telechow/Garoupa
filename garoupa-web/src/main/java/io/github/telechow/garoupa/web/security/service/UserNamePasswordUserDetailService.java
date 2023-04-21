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
package io.github.telechow.garoupa.web.security.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.entity.Permission;
import io.github.telechow.garoupa.api.entity.Resource;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.web.auto.service.IPermissionAutoService;
import io.github.telechow.garoupa.web.auto.service.IResourceAutoService;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户名密码用户详情service
 *
 * @author Telechow
 * @since 2023/3/29 10:30
 */
@Service
@RequiredArgsConstructor
public class UserNamePasswordUserDetailService implements UserDetailsService {

    private final IUserAutoService userAutoService;
    private final IPermissionAutoService permissionAutoService;
    private final IResourceAutoService resourceAutoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户信息
        User user = userAutoService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, username)
                .last("limit 1")
        );
        //1.1.如果没有查询到用户实体，抛出异常
        Optional.ofNullable(user)
                .orElseThrow(() -> new UsernameNotFoundException(ResponseCode.AUTHENTICATION_USER_NOT_EXIST.getMsg()));

        //2.查询权限信息
        List<Permission> permissions = permissionAutoService.listPermissionByUserIdPutCache(user.getId());
        List<String> permissionCodes = permissions.stream()
                .map(Permission::getPermissionCode)
                .toList();

        //3.查询资源信息
        List<Resource> resources = resourceAutoService.listResourceByUserIdPutCache(user.getId());
        List<String> resourceCodes = resources.stream()
                .map(Resource::getResourceCode)
                .toList();

        //4.将用户信息封装成UserDetails返回
        return new GaroupaUser(user, permissionCodes, resourceCodes);
    }
}
