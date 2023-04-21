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

import io.github.telechow.garoupa.api.dto.common.CommonSetLockDto;
import io.github.telechow.garoupa.api.dto.system.param.category.CreateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.dto.system.param.category.UpdateSystemParamCategoryDto;
import io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo;

import java.util.List;

/**
 * 系统参数分类service接口
 *
 * @author Telechow
 * @since 2023/4/6 17:41
 */
public interface ISystemParamCategory {

    /**
     * 创建系统参数分类
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建系统参数分类dto
     * @author Telechow
     * @since 2023/4/6 16:32
     */
    void create(CreateSystemParamCategoryDto dto);

    /**
     * 修改系统参数分类
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     * <li>需要保证参数分类名称的唯一性</li>
     * <li>如果系统参数分类被锁定，则不允许修改</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统参数分类id
     * @param dto 修改系统参数分类dto
     * @author Telechow
     * @since 2023/4/7 14:39
     */
    void update(long id, UpdateSystemParamCategoryDto dto);

    /**
     * 删除系统参数分类
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     * <li>如果系统参数分类被锁定，则不允许删除</li>
     * <li>如果系统参数分类中存在系统参数，则不允许删除</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 系统参数分类id
     * @author Telechow
     * @since 2023/4/7 16:23
     */
    void logicDelete(long id);

    /**
     * 查询系统参数分类列表
     * <li>根据创建时间升序排序</li>
     *
     * @return java.util.List < io.github.telechow.garoupa.api.vo.system.param.category.SystemParamCategoryVo> 系统参数分类vo列表
     * @author Telechow
     * @since 2023/4/7 16:48
     */
    List<SystemParamCategoryVo> list();

    /**
     * 设置系统参数分类的锁定标识
     * <li>此接口要根据系统参数分类id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统参数分类id
     * @param dto 通用设置锁定dto
     * @author Telechow
     * @since 2023/4/8 22:49
     */
    void setLock(long id, CommonSetLockDto dto);
}
