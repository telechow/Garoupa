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
package io.github.telechow.garoupa.api.vo.audit.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审计日志分页vo
 *
 * @author Telechow
 * @since 2023/4/20 10:00
 */
@Data
@Accessors(chain = true)
@Schema(description = "审计日志分页vo")
public class AuditLogPageVo implements Serializable {

    @Schema(description = "审计日志id")
    private Long id;

    @Schema(description = "审计日志标题，非空；限250字")
    private String title;

    @Schema(description = "请求用户id，非空")
    private Long userId;

    @Schema(description = "请求用户ip地址，非空")
    private String remoteIp;

    @Schema(description = "用户代理，非空")
    private String userAgent;

    @Schema(description = "请求uri，非空")
    private String requestUri;

    @Schema(description = "请求方法，非空；http的请求方法")
    private String method;

    @Schema(description = "请求参数的json，非空")
    private String requestParams;

    @Schema(description = "执行时长，非空；单位毫秒")
    private Long executeDuration;

    @Schema(description = "日志类型，非空；0：正常；10：错误")
    private Integer logType;

    @Schema(description = "日志类型名称")
    private String logTypeName;

    @Schema(description = "异常信息，只有日志类型为错误才存在异常信息")
    private String exceptionMessage;

    @Schema(description = "请求时间，非空")
    private LocalDateTime createTime;
}
