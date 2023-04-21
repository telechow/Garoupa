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

import io.github.telechow.garoupa.api.entity.SystemParam;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.github.telechow.garoupa.api.enums.system.param.SystemParamEnum;
import io.github.telechow.garoupa.web.auto.service.ISystemParamAutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 系统参数帮助类
 *
 * @author Telechow
 * @since 2023/4/18 16:33
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemParamHelper {

    private final ISystemParamAutoService systemParamAutoService;

    /**
     * 获取整型系统参数值
     * <li>如果数据库中的参数值获取失败，则会从枚举中获取默认值</li>
     *
     * @param systemParamEnum 系统参数枚举
     * @return java.lang.Integer 整型系统参数值
     * @author Telechow
     * @since 2023/4/9 16:06
     */
    public Integer getIntegerValue(SystemParamEnum systemParamEnum) {
        //1.查询系统参数实体
        SystemParam systemParam = systemParamAutoService.getSystemParamByParamCodePutCache(systemParamEnum.getCode());
        //1.1.如果查询不到系统参数实体，则写入警告日志，并返回转换枚举中的默认值
        if (Objects.isNull(systemParam)) {
            log.warn(ResponseCode.SYSTEM_PARAM_PARSING_ERROR.getMsg() + systemParamEnum.getCode());
            return Integer.valueOf(systemParamEnum.getDefaultValue());
        }

        //2.转换系统参数值
        try {
            return Integer.valueOf(systemParam.getParamValue());
        } catch (NumberFormatException e) {
            log.warn(ResponseCode.SYSTEM_PARAM_PARSING_ERROR.getMsg() + systemParamEnum.getCode());
            return Integer.valueOf(systemParamEnum.getDefaultValue());
        }
    }

    /**
     * 获取长整型系统参数值
     * <li>如果数据库中的参数值获取失败，则会从枚举中获取默认值</li>
     *
     * @param systemParamEnum 系统参数枚举
     * @return java.lang.Long 长整型系统参数值
     * @author Telechow
     * @since 2023/4/9 17:05
     */
    public Long getLongValue(SystemParamEnum systemParamEnum) {
        //1.查询系统参数实体
        SystemParam systemParam = systemParamAutoService.getSystemParamByParamCodePutCache(systemParamEnum.getCode());
        //1.1.如果查询不到系统参数实体，则写入警告日志，并返回转换枚举中的默认值
        if (Objects.isNull(systemParam)) {
            log.warn(ResponseCode.SYSTEM_PARAM_PARSING_ERROR.getMsg() + systemParamEnum.getCode());
            return Long.valueOf(systemParamEnum.getDefaultValue());
        }

        //2.转换系统参数值
        try {
            return Long.valueOf(systemParam.getParamValue());
        } catch (NumberFormatException e) {
            log.warn(ResponseCode.SYSTEM_PARAM_PARSING_ERROR.getMsg() + systemParamEnum.getCode());
            return Long.valueOf(systemParamEnum.getDefaultValue());
        }
    }

    /**
     * 获取字符串系统参数值
     * <li>如果数据库中的参数值获取失败，则会从枚举中获取默认值</li>
     *
     * @param systemParamEnum 系统参数枚举
     * @return java.lang.String 字符串系统参数值
     * @author Telechow
     * @since 2023/4/9 17:06
     */
    public String getStringValue(SystemParamEnum systemParamEnum) {
        //1.查询系统参数实体
        SystemParam systemParam = systemParamAutoService.getSystemParamByParamCodePutCache(systemParamEnum.getCode());
        //1.1.如果查询不到系统参数实体，则写入警告日志，并返回转换枚举中的默认值
        if (Objects.isNull(systemParam)) {
            log.warn(ResponseCode.SYSTEM_PARAM_PARSING_ERROR.getMsg() + systemParamEnum.getCode());
            return systemParamEnum.getDefaultValue();
        }

        //2.返回系统参数值
        return systemParam.getParamValue();
    }

}
