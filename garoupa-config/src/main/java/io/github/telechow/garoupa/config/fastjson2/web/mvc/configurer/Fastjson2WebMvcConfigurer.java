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
package io.github.telechow.garoupa.config.fastjson2.web.mvc.configurer;

import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.alibaba.fastjson2.support.spring6.webservlet.view.FastJsonJsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * Fastjson2整合SpringMVC
 * <li>使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter 以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。</li>
 * <li>使用 FastJsonJsonView 来设置 Spring MVC 默认的视图模型解析器，以提高 @Controller @ResponseBody ModelAndView JSON序列化速度。</li>
 *
 * @author Telechow
 * @since 2023/3/21 11:26
 */
@Configuration
@RequiredArgsConstructor
public class Fastjson2WebMvcConfigurer implements WebMvcConfigurer {

    private final FastJsonConfig fastJsonConfig;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //1.使用io.github.telechow.garoupa.config.fastjson2.Fastjson2Configuration中的配置
        converter.setFastJsonConfig(fastJsonConfig);
        //2.设置字符集为UTF8
        converter.setDefaultCharset(StandardCharsets.UTF_8);
        //3.设置支持的MediaType为application/json
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        converters.add(0, converter);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
        //1.使用io.github.telechow.garoupa.config.fastjson2.Fastjson2Configuration中的配置
        fastJsonJsonView.setFastJsonConfig(fastJsonConfig);
        registry.enableContentNegotiation(fastJsonJsonView);
    }
}
