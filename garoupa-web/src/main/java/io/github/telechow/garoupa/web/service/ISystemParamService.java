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
import io.github.telechow.garoupa.api.dto.system.param.CreateSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.PageSystemParamDto;
import io.github.telechow.garoupa.api.dto.system.param.SetSystemParamLockDto;
import io.github.telechow.garoupa.api.dto.system.param.UpdateSystemParamDto;
import io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo;

/**
 * 系统参数service接口
 *
 * @author Telechow
 * @since 2023/4/6 15:09
 */
public interface ISystemParamService {

    /**
     * 创建系统参数
     * <li>此接口要根据当前登录用户id开启分布式锁</li>
     * <li>需要保证参数编码、参数名称的唯一性</li>
     * <li>需要验证系统参数分类是否存在</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param dto 创建系统参数dto
     * @author Telechow
     * @since 2023/4/7 19:37
     */
    void create(CreateSystemParamDto dto);

    /**
     * 修改系统参数
     * <li>此接口要根据系统参数id开启分布式锁</li>
     * <li>需要保证参数编码、参数名称的唯一性</li>
     * <li>不允许修改系统参数的分类</li>
     * <li>如果系统参数是被锁定，则不允许修改</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统参数id
     * @param dto 修改系统参数dto
     * @author Telechow
     * @since 2023/4/7 21:32
     */
    void update(long id, UpdateSystemParamDto dto);

    /**
     * 逻辑删除系统参数
     * <li>此接口要根据系统参数id开启分布式锁</li>
     * <li>如果系统参数是被锁定，则不允许删除</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id 系统参数id
     * @author Telechow
     * @since 2023/4/7 21:55
     */
    void logicDelete(long id);

    /**
     * 根据系统参数分类分页查询系统参数
     * <li>按照系统参数编码升序排序</li>
     *
     * @param dto 分页查询系统参数dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo> 分页的系统参数分页vo
     * @author Telechow
     * @since 2023/4/7 22:30
     */
    Page<SystemParamPageVo> pageByCategory(PageSystemParamDto dto);

    /**
     * 设置系统参数的锁定标识
     * <li>此接口要根据系统参数id开启分布式锁</li>
     * <li color=red>此方法要开启事务</li>
     *
     * @param id  系统参数id
     * @param dto 设置系统参数锁定标识dto
     * @author Telechow
     * @since 2023/4/8 20:23
     */
    void setLock(long id, SetSystemParamLockDto dto);
}
