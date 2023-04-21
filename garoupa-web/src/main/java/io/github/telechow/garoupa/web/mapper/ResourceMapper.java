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
package io.github.telechow.garoupa.web.mapper;

import io.github.telechow.garoupa.api.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 资源（菜单或按钮）表 Mapper 接口
 * </p>
 *
 * @author telechow
 * @since 2023-03-25
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据用户id查询资源实体列表
     *
     * @param userId 用户id
     * @return java.util.List<io.github.telechow.garoupa.api.entity.Resource> 资源实体列表
     * @author Telechow
     * @since 2023/3/30 22:49
     */
    List<Resource> listResourceByUserId(@Param("userId") Long userId);
}
