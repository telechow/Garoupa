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
import cn.hutool.core.util.StrUtil;
import io.github.telechow.garoupa.api.dto.system.param.category.CreateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.entity.SystemParamCategory;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 系统参数分类包装器
 *
 * @author Telechow
 * @since 2023/4/6 21:45
 */
@Component
@RequiredArgsConstructor
public class SystemParamCategoryWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 创建系统参数分类dto 实例化 系统参数分类实体
     *
     * @param dto 创建系统参数分类dto
     * @return io.github.telechow.garoupa.api.entity.SystemParamCategory 系统参数分类实体
     * @author Telechow
     * @since 2023/4/6 21:46
     */
    public SystemParamCategory instance(@Nonnull CreateSystemParamCategoryDto dto) {
        SystemParamCategory systemParamCategory = new SystemParamCategory();
        systemParamCategory.setCategoryName(dto.getCategoryName());
        //设置锁定标识，能够通过接口创建的系统参数分类锁定标识都为false
        systemParamCategory.setLockSign(LockSignEnum.UNLOCK.getCode());
        return systemParamCategory;
    }

    /**
     * 将 系统参数分类实体集合 包装成 系统参数分类vo列表
     *
     * @param systemParamCategoryCollection 系统参数分类实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo> 系统参数分类vo列表
     * @author Telechow
     * @since 2023/4/7 16:56
     */
    public List<SystemParamCategoryVo> systemParamCategoryCollectionToSystemParamCategoryVoList(
            Collection<SystemParamCategory> systemParamCategoryCollection) {
        //1.如果系统参数分类实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(systemParamCategoryCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.LOCK_SIGN.getDictCode());

        //3.包装数据
        List<SystemParamCategoryVo> systemParamCategoryVoList = new ArrayList<>(systemParamCategoryCollection.size());
        for (SystemParamCategory systemParamCategory : systemParamCategoryCollection) {
            SystemParamCategoryVo vo = new SystemParamCategoryVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(systemParamCategory.getId()).setCategoryName(systemParamCategory.getCategoryName())
                    .setLockSign(systemParamCategory.getLockSign());
            //3.2.包装锁定标识名称
            Optional.ofNullable(vo.getLockSign())
                    .flatMap(ls -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), ls.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setLockSignName(sdi.getItemText()));
            systemParamCategoryVoList.add(vo);
        }
        return systemParamCategoryVoList;
    }

}
