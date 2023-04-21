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
package io.github.telechow.garoupa.web.security.cors;

import io.github.telechow.garoupa.web.security.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 *
 * @author Telechow
 * @since 2023/4/4 14:15
 */
@Configuration
@RequiredArgsConstructor
public class GaroupaCorsConfiguration {

    private final CorsProperties corsProperties;

    /**
     * 跨域配置资源的Bean
     *
     * @return org.springframework.web.cors.CorsConfigurationSource 跨域配置资源的Bean
     * @author Telechow
     * @since 2023/4/4 14:25
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域的请求头
        corsConfiguration.setAllowedHeaders(corsProperties.getAllowHeaders());
        //允许跨域的请求方法
        corsConfiguration.setAllowedMethods(corsProperties.getAllowMethods());
        //允许跨域的域
        corsConfiguration.setAllowedOrigins(corsProperties.getAllowOrigins());
        //是否允许cookie
        corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
        //一次Option请求的时间段
        corsConfiguration.setMaxAge(corsProperties.getMaxAge());

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
