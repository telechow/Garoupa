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
package io.github.telechow.garoupa.web.service;

import io.github.telechow.garoupa.api.dto.common.CommonSetEffectiveStatusDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.CreateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.dict.item.UpdateSystemDictItemDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;

import java.util.List;

/**
 * 系统字典项service接口
 *
 * @author Telechow
 * @since 2023/4/11 22:18
 */
public interface ISystemDictItemService {

    /**
     * 创建系统字典项
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证同一个系统字典中的字典项值的唯一性</li>
     * <li>需要验证系统字典是否存在</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建系统字典项dto
     * @author Telechow
     * @since 2023/4/11 22:17
     */
    void create(CreateSystemDictItemDto dto);

    /**
     * 修改系统字典项
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li>需要保证同一个系统字典中的字典项值的唯一性</li>
     * <li>不允许修改系统字典项的系统字典id</li>
     * <li>如果系统字典项是被锁定，则不允许修改</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统字典项id
     * @param dto 修改系统字典项dto
     * @author Telechow
     * @since 2023/4/11 23:33
     */
    void update(long id, UpdateSystemDictItemDto dto);

    /**
     * 逻辑删除系统字典项
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li>如果系统字典项是被锁定，则不允许删除</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 系统字典项id
     * @author Telechow
     * @since 2023/4/11 23:48
     */
    void logicDelete(long id);

    /**
     * 设置系统字典项的锁定标识
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统字典项id
     * @param dto 设置系统参数锁定标识dto
     * @author Telechow
     * @since 2023/4/12 13:15
     */
    void setLock(long id, SetSystemParamLockDto dto);

    /**
     * 设置系统字典项的有效状态
     * <li>此接口要根据系统字典项id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统字典项id
     * @param dto 通用设置有效状态dto
     * @author Telechow
     * @since 2023/4/12 13:49
     */
    void setEffectiveStatus(long id, CommonSetEffectiveStatusDto dto);

    /**
     * 根据系统字典id查询系统字典项列表
     * <li>按照排序升序、创建时间升序排序</li>
     *
     * @param systemDictId 系统字典id
     * @return java.util.List < io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemPageVo> 系统字典项分页vo列表
     * @author Telechow
     * @since 2023/4/12 15:30
     */
    List<SystemDictItemPageVo> listBySystemDictId(long systemDictId);

    /**
     * 根据系统字典编码查询系统字典项列表
     * <li>按照排序升序、创建时间升序排序</li>
     * <li>此接口常用在下拉选择查询条件等位置</li>
     *
     * @param dictCode 系统字典编码
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo> 系统字典项vo列表
     * @author Telechow
     * @since 2023/4/12 16:22
     */
    List<SystemDictItemVo> listBySystemDictCode(String dictCode);
}
