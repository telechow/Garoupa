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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.dict.CreateSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.PageSystemDictDto;
import io.github.telechow.garoupa.api.dto.system.dict.UpdateSystemDictDto;
import io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo;

/**
 * 系统字典service接口
 *
 * @author Telechow
 * @since 2023/4/10 21:14
 */
public interface ISystemDictService {

    /**
     * 创建系统字典
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证字典编码的唯一性</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建系统字典dto
     * @author Telechow
     * @since 2023/4/10 21:31
     */
    void create(CreateSystemDictDto dto);

    /**
     * 修改系统字典
     * <li>此接口要根据系统字典id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     * <li>如果系统分类被锁定，则不允许修改</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统字典id
     * @param dto 修改系统字典dto
     * @author Telechow
     * @since 2023/4/10 23:00
     */
    void update(long id, UpdateSystemDictDto dto);

    /**
     * 删除系统字典
     * <li>此接口要根据系统字典id开启分布式锁</li>
     * <li>如果系统字典被锁定，则不允许删除</li>
     * <li>如果系统字典中存在系统字典项，则不允许删除</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 系统字典id
     * @author Telechow
     * @since 2023/4/11 15:47
     */
    void logicDelete(long id);

    /**
     * 设置系统字典的锁定标识
     * <li>此接口要根据系统字典id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统字典id
     * @param dto 通用设置锁定dto
     * @author Telechow
     * @since 2023/4/11 16:08
     */
    void setLock(long id, CommonSetLockDto dto);

    /**
     * 分页查询系统字典
     * <li>根据排序升序、字典编码升序排序</li>
     *
     * @param dto 分页查询系统字典dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.system.dict.SystemDictPageVo> 分页的系统字典分页vo
     * @author Telechow
     * @since 2023/4/11 17:03
     */
    Page<SystemDictPageVo> page(PageSystemDictDto dto);
}
