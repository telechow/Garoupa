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
import io.github.telechow.garoupa.api.dto.system.param.CreateSystemParamDto;
import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.api.enums.system.dict.SystemDictEnum;
import io.github.telechow.garoupa.api.enums.common.LockSignEnum;
import io.github.telechow.garoupa.api.vo.system.dict.item.SystemDictItemVo;
import io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo;
import io.github.telechow.garoupa.web.auto.service.ISystemParamCategoryAutoService;
import io.github.telechow.garoupa.web.helper.SystemDictHelper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 系统参数包装器
 *
 * @author Telechow
 * @since 2023/4/7 21:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemParamWrapper {

    private final ISystemParamCategoryAutoService systemParamCategoryAutoService;
    private final SystemDictHelper systemDictHelper;

    /**
     * 根据 创建系统参数dto 实例化 系统参数实体
     *
     * @param dto 创建系统参数dto
     * @return io.github.telechow.garoupa.api.entity.SystemParam 系统参数实体
     * @author Telechow
     * @since 2023/4/7 21:06
     */
    public SystemParam instance(@Nonnull CreateSystemParamDto dto) {
        SystemParam systemParam = new SystemParam();
        systemParam.setParamCategoryId(dto.getParamCategoryId()).setParamCode(dto.getParamCode())
                .setParamName(dto.getParamName()).setParamValue(dto.getParamValue());
        //设置锁定标识，能够通过接口创建的系统参数锁定标识都为false
        systemParam.setLockSign(LockSignEnum.UNLOCK.getCode());
        return systemParam;
    }

    /**
     * 将 系统参数实体集合 包装成 系统参数分页vo列表
     *
     * @param systemParamCollection 系统参数实体集合
     * @return java.util.List<io.github.telechow.garoupa.api.vo.system.param.SystemParamPageVo> 系统参数分页vo列表
     * @author Telechow
     * @since 2023/4/7 23:26
     */
    public List<SystemParamPageVo> systemParamCollectionToSystemParamPageVoList(
            Collection<SystemParam> systemParamCollection) {
        //1.如果系统参数实体集合为空，直接返回空列表
        if (CollectionUtil.isEmpty(systemParamCollection)) {
            return Collections.emptyList();
        }

        //2.数据准备
        List<SystemDictItemVo> systemDictItemVos = systemDictHelper.listSystemDictItemByDictCode(
                SystemDictEnum.LOCK_SIGN.getDictCode());

        //3.包装数据
        List<SystemParamPageVo> systemParamPageVoList = new ArrayList<>(systemParamCollection.size());
        for (SystemParam systemParam : systemParamCollection) {
            SystemParamPageVo vo = new SystemParamPageVo();
            //3.1.复制同类型同名称的属性值
            vo.setId(systemParam.getId()).setParamCategoryId(systemParam.getParamCategoryId())
                    .setParamCode(systemParam.getParamCode()).setParamName(systemParam.getParamName())
                    .setParamValue(systemParam.getParamValue()).setLockSign(systemParam.getLockSign());
            //3.2.包装系统参数分类名称
            Optional.ofNullable(vo.getParamCategoryId())
                    .flatMap(pcid -> Optional.ofNullable(systemParamCategoryAutoService.getByIdPutCache(pcid)))
                    .ifPresent(spc -> vo.setCategoryName(spc.getCategoryName()));
            //3.3.包装锁定标识名称
            Optional.ofNullable(vo.getLockSign())
                    .flatMap(ls -> systemDictItemVos.stream()
                            .filter(sdi -> StrUtil.equals(sdi.getItemValue(), ls.toString()))
                            .findFirst())
                    .ifPresent(sdi -> vo.setLockSignName(sdi.getItemText()));
            systemParamPageVoList.add(vo);
        }
        return systemParamPageVoList;
    }

}
