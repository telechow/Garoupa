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
import io.github.telechow.garoupa.api.dto.user.PageUserDto;
import io.github.telechow.garoupa.api.dto.user.UserAssociateRoleDto;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.entity.UserRoleRelation;
import io.github.telechow.garoupa.api.enums.rbac.user.UserTypeEnum;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;
import io.github.telechow.garoupa.web.auto.service.IUserAutoService;
import io.github.telechow.garoupa.web.auto.service.IUserRoleRelationAutoService;
import io.github.telechow.garoupa.web.service.ICustomService;
import io.github.telechow.garoupa.web.validator.CustomValidator;
import io.github.telechow.garoupa.web.validator.RoleValidator;
import io.github.telechow.garoupa.web.wrapper.UserRoleRelationWrapper;
import io.github.telechow.garoupa.web.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户service实现
 *
 * @author Telechow
 * @since 2023/4/17 16:19
 */
@Service
@RequiredArgsConstructor
public class CustomServiceImpl implements ICustomService {

    private final CustomValidator customValidator;
    private final RoleValidator roleValidator;

    private final UserRoleRelationWrapper userRoleRelationWrapper;
    private final UserWrapper userWrapper;

    private final IUserAutoService userAutoService;
    private final IUserRoleRelationAutoService userRoleRelationAutoService;

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(long id) {
        //1.数据验证
        //1.1.验证客户是否存在，如果不存在抛出异常
        customValidator.existById(id);

        //2.逻辑删除客户实体
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
    public void associateRole(long id, UserAssociateRoleDto dto) {
        //1.数据验证
        //1.1.验证客户是否存在，如果不存在抛出异常
        customValidator.existById(id);

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
                //1.1.只查询客户
                .eq(User::getUserType, UserTypeEnum.CUSTOM.getCode())
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
}
