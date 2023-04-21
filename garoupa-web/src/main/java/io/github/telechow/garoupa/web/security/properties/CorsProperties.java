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
package io.github.telechow.garoupa.web.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

/**
 * 跨域配置参数
 *
 * @author Telechow
 * @since 2023/4/9 20:59
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "garoupa.cors")
public class CorsProperties {

    /**
     * 允许的请求头
     */
    private List<String> allowHeaders;

    /**
     * 允许的请求方法
     */
    private List<String> allowMethods;

    /**
     * 允许的域
     */
    private List<String> allowOrigins;

    /**
     * 是否允许cookie
     */
    private Boolean allowCredentials;

    /**
     * 最大生存时间
     */
    private Duration maxAge;
}
