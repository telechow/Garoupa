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
import io.github.telechow.garoupa.api.dto.system.dict.CreateSystemDictDto;
import io.github.telechow.garoupa.api.entity.SystemDict;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 系统字典包装器
 *
 * @author Telechow
 * @since 2023/4/10 22:38
 */
@Component
@RequiredArgsConstructor
public class SystemDictWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 创建系统字典dto 实例化 系统字典实体
     *
     * @param dto 创建系统字典dto
     * @return io.github.telechow.garoupa.api.entity.SystemDict 系统字典实体
     * @author Telechow
     * @since 2023/4/10 22:45
     */
    public SystemDict instance(@Nonnull CreateSystemDictDto dto) {
        SystemDict systemDict = new SystemDict();
        //1.复制同类型同名称的属性值
        systemDict.setDictCode(dto.getDictCode()).setDictName(dto.getDictName()).setSortNumber(dto.getSortNumber());

        //2.包装锁定标识，通过接口创建的系统字典锁定标识都为false
        systemDict.setLockSign(LockSignEnum.UNLOCK.getCode());
        return systemDict;
    }

    /**
     * 将 系统字典实体集合 包装成 系统字典分页vo列表
     *
     * @param systemDictCollection 系统字典实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo> 系统字典分页vo列表
     * @author Telechow
     * @since 2023/4/11 18:04
     */
    public List<SystemDictPageVo> systemDictCollectionToSystemDictPageVoList(
            Collection<SystemDict> systemDictCollection) {
        //1.如果系统字典实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(systemDictCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.LOCK_SIGN.getDictCode());

        //3.包装数据
        List<SystemDictPageVo> systemDictPageVoList = new ArrayList<>(systemDictCollection.size());
        for (SystemDict systemDict : systemDictCollection) {
            SystemDictPageVo vo = new SystemDictPageVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(systemDict.getId()).setDictCode(systemDict.getDictCode()).setDictName(systemDict.getDictName())
                    .setDictDescribe(systemDict.getDictDescribe()).setLockSign(systemDict.getLockSign());
            //3.2.包装锁定标识名称
            Optional.ofNullable(vo.getLockSign())
                    .flatMap(ls -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), ls.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setLockSignName(sdi.getItemText()));
            systemDictPageVoList.add(vo);
        }
        return systemDictPageVoList;
    }
}
