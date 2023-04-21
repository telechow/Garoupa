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
package io.github.telechow.garoupa.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.constant.AuthenticationCacheConstant;
import io.github.telechow.garoupa.api.dto.user.*;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.entity.UserRoleRelation;
import io.github.telechow.garoupa.api.enums.rbac.user.UserTypeEnum;
import io.github.telechow.garoupa.api.enums.system.param.SystemParamEnum;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import io.github.telechow.garoupa.web.auto.service.IUserRoleRelationAutoService;
import io.github.telechow.garoupa.web.helper.SystemParamHelper;
import io.github.telechow.garoupa.web.service.IUserService;
import io.github.telechow.garoupa.web.utils.AuthenticationUtil;
import io.github.telechow.garoupa.web.validator.CommonValidator;
import io.github.telechow.garoupa.web.validator.RoleValidator;
import io.github.telechow.garoupa.web.validator.UserValidator;
import io.github.telechow.garoupa.web.wrapper.UserRoleRelationWrapper;
import io.github.telechow.garoupa.web.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 用户service实现
 *
 * @author Telechow
 * @since 2023/4/16 13:22
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserValidator userValidator;
    private final RoleValidator roleValidator;
    private final CommonValidator commonValidator;

    private final UserWrapper userWrapper;
    private final UserRoleRelationWrapper userRoleRelationWrapper;

    private final IUserAutoService userAutoService;
    private final IUserRoleRelationAutoService userRoleRelationAutoService;

    private final SystemParamHelper systemParamHelper;

    private final PasswordEncoder passwordEncoder;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(CreateUserDto dto) {
        //1.实例化用户实体，并入库
        User user = userWrapper.instance(dto);
        userAutoService.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证用户是否存在，如果不存在抛出异常
        userValidator.existById(id);

        //2.逻辑删除用户实体
        userAutoService.removeById(id);

        //3.删除用户角色关系实体
        userRoleRelationAutoService.remove(Wrappers.<UserRoleRelation>lambdaQuery()
                .eq(UserRoleRelation::getUserId, id)
        );

        //4.清除缓存
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.USER_GET_BY_ID_CACHE_NAME))
                .ifPresent(c -> c.evict(id));
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
        Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                .ifPresent(Cache::clear);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AuthenticationCacheConstant.USER_GET_BY_ID_CACHE_NAME + "#2592000000", key = "#id")
    public void resetPassword(long id) {
        //1.数据验证
        //1.1.验证用户是否存在，如果不存在抛出异常
        userValidator.existById(id);

        //2.从系统参数中查询用户默认密码
        String userDefaultPassword = systemParamHelper.getStringValue(SystemParamEnum.RBAC_USER_DEFAULT_PASSWORD);

        //3.修改用户实体，并入库
        User updateUser = new User();
        updateUser.setId(id).setPassword(passwordEncoder.encode(userDefaultPassword));
        userAutoService.updateById(updateUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateRole(long id, UserAssociateRoleDto dto) {
        //1.数据验证
        //1.1.验证用户是否存在，如果不存在抛出异常
        userValidator.existById(id);

        //2.删除用户角色关系实体
        userRoleRelationAutoService.remove(Wrappers.<UserRoleRelation>lambdaQuery()
                .eq(UserRoleRelation::getUserId, id)
        );

        try {
            //3.如果传递的角色id集合为空，则什么都不用做，直接返回
            if (CollectionUtil.isEmpty(dto.getRoleIds())) {
                return;
            }

            //4.再次进行数据验证
            //4.1.验证角色是否全部存在，如果有一个不存在抛出异常
            roleValidator.allExistByIdCollection(dto.getRoleIds());

            //5.实例化用户角色权限实体列表，并入库
            List<UserRoleRelation> userRoleRelations = userRoleRelationWrapper.instanceList(id, dto.getRoleIds());
            userRoleRelationAutoService.saveBatch(userRoleRelations);
        } finally {
            //6.清除缓存
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.RESOURCE_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
            Optional.ofNullable(cacheManager.getCache(AuthenticationCacheConstant.PERMISSION_LIST_BY_USER_ID_CACHE_NAME))
                    .ifPresent(Cache::clear);
        }
    }

    @Override
    public Page<UserPageVo> page(PageUserDto dto) {
        //1.查询数据
        Page<User> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery()
                //1.1.只查询用户
                .eq(User::getUserType, UserTypeEnum.USER.getCode())
                //1.2.按照创建时间降序排序
                .orderByDesc(User::getCreateTime);
        //1.3.用户名模糊查询条件
        if (StrUtil.isNotBlank(dto.getUserNameLike())) {
            lambdaQueryWrapper.apply("match(user_name) against({0})", dto.getUserNameLike());
        }
        //1.4.用户昵称模糊查询条件
        if (StrUtil.isNotBlank(dto.getNickNameLike())) {
            lambdaQueryWrapper.apply("match(nick_name) against({0})", dto.getNickNameLike());
        }
        //1.5.用户身份证号查询条件
        if (StrUtil.isNotBlank(dto.getIdCardNumber())) {
            lambdaQueryWrapper.eq(User::getIdCardNumber, dto.getIdCardNumber());
        }
        //1.6.电话号码查询条件
        if (StrUtil.isNotBlank(dto.getMobile())) {
            lambdaQueryWrapper.eq(User::getMobile, dto.getMobile());
        }
        //1.7.邮箱查询条件
        if (StrUtil.isNotBlank(dto.getEmail())) {
            lambdaQueryWrapper.eq(User::getEmail, dto.getEmail());
        }
        page = userAutoService.page(page, lambdaQueryWrapper);

        //2.包装数据
        List<UserPageVo> userPageVos = userWrapper.userCollectionToUserPageVoList(page.getRecords());
        Page<UserPageVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(userPageVos);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePasswordSelf(UpdatePasswordSelfDto dto) {
        //1.数据验证
        //1.1.验证用户类型为用户，如果不是抛出异常
        User user = AuthenticationUtil.getUser();
        userValidator.userTypeIsUser(user);
        //1.2.验证两次输入的新密码是否一致，如果不一致抛出异常
        userValidator.validateUpdatePassword(dto.getNeoPassword(), dto.getNeoPasswordAgain(), dto.getOriginPassword(), user);

        //2.修改用户实体,并入库
        User updateUser = new User();
        updateUser.setId(Objects.requireNonNull(user).getId()).setPassword(passwordEncoder.encode(dto.getNeoPassword()));
        userAutoService.updateById(updateUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = AuthenticationCacheConstant.USER_GET_BY_ID_CACHE_NAME + "#2592000000"
            , key = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
    public void updateSelf(UpdateSelfDto dto) {
        //1.数据验证
        //1.1.验证用户类型为用户，如果不是抛出异常
        User user = AuthenticationUtil.getUser();
        userValidator.userTypeIsUser(user);
        //1.2.如果身份证号不为空，验证身份证号是否合法
        if (StrUtil.isNotBlank(dto.getIdCardNumber())) {
            commonValidator.validateIdCardNumber(dto.getIdCardNumber());
        }
        //1.3.如果电话号码不为空，验证电话号码是否合法
        if (StrUtil.isNotBlank(dto.getMobile())) {
            commonValidator.validateMobile(dto.getMobile());
        }
        //1.4.如果电子邮箱不为空，验证电子邮箱是否合法
        if (StrUtil.isNotBlank(dto.getEmail())) {
            commonValidator.validateEmail(dto.getEmail());
        }

        //2.修改用户实体，并入库；因为此处参数可能为空null，所以使用Wrapper设置要修改的字段
        userAutoService.update(Wrappers.<User>lambdaUpdate()
                .set(User::getNickName, dto.getNickName())
                .set(User::getAvatarUri, dto.getAvatarUri())
                .set(User::getRealName, dto.getRealName())
                .set(User::getIdCardNumber, dto.getIdCardNumber())
                .set(User::getMobile, dto.getMobile())
                .set(User::getEmail, dto.getEmail())
                .set(User::getBirthday, dto.getBirthday())
                .set(User::getGender, dto.getGender())
                .set(User::getUpdateUser, Objects.requireNonNull(user).getId())
                .set(User::getUpdateTime, LocalDateTime.now())
                .eq(User::getId, user.getId())
        );
    }
}
