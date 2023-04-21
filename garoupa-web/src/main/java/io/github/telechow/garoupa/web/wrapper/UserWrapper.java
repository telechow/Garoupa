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
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.github.telechow.garoupa.api.dto.user.CreateUserDto;
import io.github.telechow.garoupa.api.entity.User;
import io.github.telechow.garoupa.api.enums.rbac.user.UserTypeEnum;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.enums.system.param.SystemParamEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.api.vo.user.UserPageVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import io.github.telechow.garoupa.web.helper.SystemParamHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户包装器
 *
 * @author Telechow
 * @since 2023/4/16 13:26
 */
@Component
@RequiredArgsConstructor
public class UserWrapper {

    private final SystemDictHelper systemDictHelper;
    private final SystemParamHelper systemParamHelper;

    private final PasswordEncoder passwordEncoder;

    /**
     * 根据 创建用户dto 实例化 用户实体
     *
     * @param dto 创建用户dto
     * @return io.github.telechow.garoupa.api.entity.User 用户实体
     * @author Telechow
     * @since 2023/4/16 13:26
     */
    public User instance(@Nonnull CreateUserDto dto) {
        User user = new User();
        //1.包装用户名
        user.setUserName(dto.getUserName());

        //2.包装密码，从系统参数中查询用户默认密码
        String userDefaultPassword = systemParamHelper.getStringValue(SystemParamEnum.RBAC_USER_DEFAULT_PASSWORD);
        user.setPassword(passwordEncoder.encode(userDefaultPassword));

        //3.包装用户昵称，为前缀加uuid，前缀从系统参数中查询
        String nicknamePrefix = systemParamHelper.getStringValue(SystemParamEnum.RBAC_USER_DEFAULT_NICKNAME_PREFIX);
        user.setNickName(nicknamePrefix + UUID.randomUUID());

        //4.包装用户类型，设置为用户
        user.setUserType(UserTypeEnum.USER.getCode());
        return user;
    }

    /**
     * 将 用户实体集合 包装成 用户分页vo列表
     *
     * @param userCollection 用户实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.user.UserPageVo> 用户分页vo列表
     * @author Telechow
     * @since 2023/4/16 18:11
     */
    public List<UserPageVo> userCollectionToUserPageVoList(Collection<User> userCollection) {
        //1.如果用户实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(userCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        //2.1.性别系统字典数据
        List<SystemDictItemVo> genderDictItemVos = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.GENDER.getDictCode());

        //3.包装数据
        List<UserPageVo> userPageVos = new ArrayList<>(userCollection.size());
        for (User user : userCollection) {
            UserPageVo vo = new UserPageVo();
            //3.1.包装同类型同名称的属性值
            vo.setId(user.getId()).setUserName(vo.getUserName()).setNickName(user.getNickName())
                    .setAvatarUri(user.getAvatarUri()).setRealName(user.getRealName())
                    .setIdCardNumber(user.getIdCardNumber()).setMobile(user.getMobile()).setEmail(user.getEmail())
                    .setBirthday(user.getBirthday()).setGender(user.getGender()).setCreateTime(user.getCreateTime());
            //3.2.包装性别名称
            Optional.ofNullable(vo.getGender())
                    .flatMap(g -> genderDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), g.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setGenderName(sdi.getItemText()));
            userPageVos.add(vo);
        }
        return userPageVos;
    }
}
