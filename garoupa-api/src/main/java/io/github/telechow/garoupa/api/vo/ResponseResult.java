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
package io.github.telechow.garoupa.api.vo;

import io.github.telechow.garoupa.api.enums.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 响应结果对象
 *
 * @author Telechow
 * @since 2023/3/27 18:26
 */
@Data
@Accessors(chain = true)
@Schema(description = "响应结果对象")
public class ResponseResult<T> implements Serializable {

    public static final String META_CAUSE_KEY = "cause";

    /**
     * 错误码，形如：A0000
     */
    @Schema(description = "错误码，形如：A0000")
    private String code;

    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;

    /**
     * 响应消息，如果错误则为错误消息
     */
    @Schema(description = "响应消息")
    private String msg;

    /**
     * 元数据，一些特殊接口需要响应错误相关的元数据
     */
    @Schema(description = "响应元数据")
    private Map<String, Object> meta;

    /**
     * 构造方法
     *
     * @param code 错误码
     * @param data 响应数据
     * @param msg  响应消息
     * @param meta 响应元数据
     * @author Telechow
     * @since 2023/3/27 20:55
     */
    private ResponseResult(String code, T data, String msg, Map<String, Object> meta) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.meta = meta;
    }

    /**
     * 返回一个正确的响应结果，无响应数据，响应消息为ok
     *
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 20:57
     */
    public static <T> ResponseResult<T> ok() {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), null, ResponseCode.SUCCESS.getMsg(), null);
    }

    /**
     * 返回一个正确的响应结果，无响应数据，指定响应消息
     *
     * @param msg 效应消息
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 20:58
     */
    public static <T> ResponseResult<T> ok(String msg) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), null, msg, null);
    }

    /**
     * 返回一个正确的响应结果，指定响应数据，响应消息为ok
     *
     * @param data 响应数据
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:00
     */
    public static <T> ResponseResult<T> data(T data) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), data, ResponseCode.SUCCESS.getMsg(), null);
    }

    /**
     * 返回一个正确的响应结果，指定响应数据，指定响应消息
     *
     * @param data 响应数据
     * @param msg  响应消息
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:00
     */
    public static <T> ResponseResult<T> data(T data, String msg) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), data, msg, null);
    }

    /**
     * 返回一个正确的响应结果，指定响应数据，指定响应消息，指定响应元数据
     *
     * @param data 响应数据
     * @param msg  响应消息
     * @param meta 响应元数据
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:01
     */
    public static <T> ResponseResult<T> data(T data, String msg, Map<String, Object> meta) {
        return new ResponseResult<>(ResponseCode.SUCCESS.getCode(), data, msg, meta);
    }

    /**
     * 返回一个失败的响应结果，指定响应码和响应消息，无响应数据
     *
     * @param responseCode 响应码枚举
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:04
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode) {
        return new ResponseResult<>(responseCode.getCode(), null, responseCode.getMsg(), null);
    }

    /**
     * 返回一个失败的响应结果，指定响应码和响应消息，无响应数据
     *
     * @param responseCode 响应码枚举
     * @param msg          响应消息
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:04
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode, String msg) {
        return new ResponseResult<>(responseCode.getCode(), null, msg, null);
    }

    /**
     * 返回一个失败的响应结果，指定响应码和响应消息，指定响应数据
     *
     * @param responseCode 响应码枚举
     * @param data         响应数据
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:05
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode, T data) {
        return new ResponseResult<>(responseCode.getCode(), data, responseCode.getMsg(), null);
    }

    /**
     * 返回一个失败的响应结果，指定响应码和响应消息，指定响应数据，执行响应元数据
     *
     * @param responseCode 响应码枚举
     * @param data         响应数据
     * @param meta         响应元数据
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/27 21:06
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode, T data, Map<String, Object> meta) {
        return new ResponseResult<>(responseCode.getCode(), data, responseCode.getMsg(), meta);
    }

    /**
     * 返回一个失败的响应结果，指定响应码和响应消息，指定响应数据，执行响应元数据
     *
     * @param responseCode 响应码枚举
     * @param data         响应数据
     * @param msg          响应消息
     * @param meta         响应元数据
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<T> 响应结果对象
     * @author Telechow
     * @since 2023/3/30 21:43
     */
    public static <T> ResponseResult<T> fail(ResponseCode responseCode, T data, String msg, Map<String, Object> meta) {
        return new ResponseResult<>(responseCode.getCode(), data, msg, meta);
    }

}
