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

/**
 * 鉴权参数
 *
 * @author Telechow
 * @since 2023/4/1 16:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "garoupa.authorize")
public class AuthorizeProperties {

    /**
     * 可以随意访问的接口uri列表
     */
    private String[] permitAllUri;

    /**
     * 只允许匿名用户访问的接口uri
     */
    private String[] anonymousUri;

    /**
     * 禁止访问的接口uri
     */
    private String[] denyAllUri;
}
