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
package io.github.telechow.garoupa.web.helper;

import io.github.telechow.garoupa.api.entity.SystemDictItem;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.web.auto.service.ISystemDictItemAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统字典帮助类
 *
 * @author Telechow
 * @since 2023/4/18 16:51
 */
@Component
@RequiredArgsConstructor
public class SystemDictHelper {

    private final ISystemDictItemAutoService systemDictItemAutoService;

    /**
     * 根据 系统字典编码 查询 系统字典项vo列表
     *
     * @param dictCode 系统字典编码
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo> 系统字典项vo列表
     * @author Telechow
     * @since 2023/4/18 16:51
     */
    public List<SystemDictItemVo> listSystemDictItemByDictCode(String dictCode) {
        return systemDictItemAutoService.listSystemDictItemByDictCodePutCache(dictCode, false);
    }

    /**
     * 根据 系统字典编码、系统字典项值 查询 系统字典项实体
     *
     * @param dictCode  系统字典编码
     * @param itemValue 系统字典项值
     * @return io.github.telechow.garoupa.api.entity.SystemDictItem 系统字典项实体
     * @author Telechow
     * @since 2023/4/12 14:15
     */
    public SystemDictItem getByDictCodeAndItemValue(String dictCode, String itemValue) {
        return systemDictItemAutoService.getByDictCodeAndItemValuePutCache(dictCode, itemValue);
    }
}
