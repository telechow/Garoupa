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
import io.github.telechow.garoupa.api.dto.system.dict.item.CreateSystemDictItemDto;
import io.github.telechow.garoupa.api.entity.SystemDict;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.common.EffectiveStatusEnum;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 系统字典项包装器
 *
 * @author Telechow
 * @since 2023/4/11 22:27
 */
@Component
@RequiredArgsConstructor
public class SystemDictItemWrapper {

    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 创建系统字典项dto 实例化 系统字典项实体
     *
     * @param dto        创建系统字典项dto
     * @param systemDict 系统字典实体
     * @return io.github.telechow.garoupa.api.entity.SystemDictItem 系统字典项实体
     * @author Telechow
     * @since 2023/4/11 22:28
     */
    public SystemDictItem instance(@Nonnull CreateSystemDictItemDto dto, @Nonnull SystemDict systemDict) {
        SystemDictItem systemDictItem = new SystemDictItem();
        //1.复制同类型同名称的属性值
        systemDictItem.setDictId(dto.getDictId()).setItemValue(dto.getItemValue()).setItemText(dto.getItemText())
                .setSortNumber(dto.getSortNumber());

        //2.包装字典编码和字典名称，冗余属性
        systemDictItem.setDictCode(systemDict.getDictCode()).setDictName(systemDict.getDictName());

        //3.包装有效状态，设置为有效
        systemDictItem.setEffectiveStatus(EffectiveStatusEnum.EFFECTIVE.getCode());

        //4.包装锁定标识，设置为未锁定
        systemDictItem.setLockSign(LockSignEnum.UNLOCK.getCode());
        return systemDictItem;
    }

    /**
     * 将 系统字典项实体集合 包装成 系统字典项分页vo列表
     *
     * @param systemDictItemCollection 系统字典项实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo> 系统字典项分页vo列表
     * @author Telechow
     * @since 2023/4/12 15:39
     */
    public List<SystemDictItemPageVo> systemDictItemCollectionToSystemDictItemPageVoList(
            Collection<SystemDictItem> systemDictItemCollection) {
        //1.如果系统字典项实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(systemDictItemCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        //2.1.有效状态系统字典数据
        List<SystemDictItemVo> effectiveStatusDictItems = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.EFFECTIVE_STATUS.getDictCode());
        //2.2.锁定标识系统字典数据
        List<SystemDictItemVo> lockSignDictItems = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.LOCK_SIGN.getDictCode());

        //2.包装数据
        List<SystemDictItemPageVo> systemDictItemPageVoList = new ArrayList<>(systemDictItemCollection.size());
        for (SystemDictItem systemDictItem : systemDictItemCollection) {
            SystemDictItemPageVo vo = new SystemDictItemPageVo();
            //2.1.复制同类型同名称的属性值
            vo.setId(systemDictItem.getId()).setDictId(systemDictItem.getDictId())
                    .setDictCode(systemDictItem.getDictCode()).setDictName(systemDictItem.getDictName())
                    .setItemValue(systemDictItem.getItemValue()).setItemText(systemDictItem.getItemText())
                    .setSortNumber(systemDictItem.getSortNumber()).setEffectiveStatus(systemDictItem.getEffectiveStatus())
                    .setLockSign(systemDictItem.getLockSign());
            //2.2.包装有效状态名称
            Optional.ofNullable(vo.getEffectiveStatus())
                    .flatMap(es -> effectiveStatusDictItems.stream()
                            .filter(di -> StrUtil.equals(di.getItemValue(), vo.getEffectiveStatus().toString()))
                            .findFirst())
                    .ifPresent(di -> vo.setEffectiveStatusName(di.getItemText()));
            //2.3.包装锁定标识名称
            Optional.ofNullable(vo.getLockSign())
                    .flatMap(es -> lockSignDictItems.stream()
                            .filter(di -> StrUtil.equals(di.getItemValue(), vo.getLockSign().toString()))
                            .findFirst())
                    .ifPresent(di -> vo.setLockSignName(di.getItemText()));
            systemDictItemPageVoList.add(vo);
        }
        return systemDictItemPageVoList;
    }
}
