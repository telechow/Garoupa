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
package io.github.telechow.garoupa.web.auto.service;

import io.github.telechow.garoupa.api.entity.SystemDictItem;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;

import java.util.List;

/**
 * <p>
 * 系统字典项表 服务类
 * </p>
 *
 * @author telechow
 * @since 2023-04-10
 */
public interface ISystemDictItemAutoService extends IService<SystemDictItem> {

    /**
     * 根据 系统字典编码、系统字典项值 查询 系统字典项实体，并写入缓存
     *
     * @param dictCode  系统字典编码
     * @param itemValue 系统字典项值
     * @return io.github.telechow.garoupa.api.entity.SystemDictItem 系统字典项实体
     * @author Telechow
     * @since 2023/4/12 14:15
     */
    SystemDictItem getByDictCodeAndItemValuePutCache(String dictCode, String itemValue);

    /**
     * 根据 系统字典编码 查询 系统字典项vo列表，并写入缓存
     * <li>按照排序升序、创建时间升序排序</li>
     *
     * @param dictCode      系统字典编码
     * @param onlyEffective 是否只查询有效的数据
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo> 系统字典项vo列表
     * @author Telechow
     * @since 2023/4/12 15:50
     */
    List<SystemDictItemVo> listSystemDictItemByDictCodePutCache(String dictCode, boolean onlyEffective);
}
