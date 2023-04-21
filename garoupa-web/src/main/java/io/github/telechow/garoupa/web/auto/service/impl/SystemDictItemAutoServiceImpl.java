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
package io.github.telechow.garoupa.web.auto.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.telechow.garoupa.api.constant.SystemDictCacheConstant;
import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.enums.common.EffectiveStatusEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.mapper.SystemDictItemMapper;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统字典项表 服务实现类
 * </p>
 *
 * @author telechow
 * @since 2023-04-10
 */
@Service
public class SystemDictItemAutoServiceImpl extends ServiceImpl<SystemDictItemMapper, SystemDictItem>
        implements ISystemDictItemAutoService {

    @Override
    @Cacheable(value = SystemDictCacheConstant.SYSTEM_DICT_ITEM_GET_BY_DICT_CODE_AND_ITEM_VALUE_CACHE_NAME + "#2592000000"
            , key = "#dictCode + ':' + #itemValue", unless = "#result == null")
    public SystemDictItem getByDictCodeAndItemValuePutCache(String dictCode, String itemValue) {
        return this.getBaseMapper().selectOne(Wrappers.<SystemDictItem>lambdaQuery()
                .eq(SystemDictItem::getDictCode, dictCode)
                .eq(SystemDictItem::getItemValue, itemValue)
                .last("limit 1")
        );
    }

    @Override
    @Cacheable(value = SystemDictCacheConstant.SYSTEM_DICT_ITEM_LIST_BY_DICT_CODE_CACHE_NAME + "#2592000000"
            , key = "#dictCode + ':' + #onlyEffective", unless = "T(cn.hutool.core.collection.CollectionUtil).isEmpty(#result)")
    public List<SystemDictItemVo> listSystemDictItemByDictCodePutCache(String dictCode, boolean onlyEffective) {
        //1.构造查询条件
        LambdaQueryWrapper<SystemDictItem> lambdaQueryWrapper = Wrappers.<SystemDictItem>lambdaQuery()
                .select(SystemDictItem::getId, SystemDictItem::getItemValue, SystemDictItem::getItemText)
                //1.1.系统字典编码查询条件
                .eq(SystemDictItem::getDictCode, dictCode)
                //1.2.按照排序升序、创建时间升序排序
                .orderByAsc(SystemDictItem::getSortNumber)
                .orderByAsc(SystemDictItem::getCreateTime);
        //1.3.是否只查询有效的数据
        if (onlyEffective) {
            lambdaQueryWrapper.eq(SystemDictItem::getEffectiveStatus, EffectiveStatusEnum.EFFECTIVE.getCode());
        }

        //2.包装数据
        List<SystemDictItem> systemDictItems = this.getBaseMapper().selectList(lambdaQueryWrapper);
        return systemDictItems.stream()
                .map(syi -> {
                    SystemDictItemVo vo = new SystemDictItemVo();
                    vo.setId(syi.getId()).setItemValue(syi.getItemValue()).setItemText(syi.getItemText());
                    return vo;
                })
                .toList();
    }

}
