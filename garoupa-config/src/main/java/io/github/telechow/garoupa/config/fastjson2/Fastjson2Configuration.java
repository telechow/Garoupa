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
package io.github.telechow.garoupa.config.fastjson2;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * fastjson2配置
 *
 * @author Telechow
 * @since 2023/3/21 10:44
 */
@Configuration
public class Fastjson2Configuration {

    static {
        //全局配置json序列化与反序列化特性
        JSONWriter.Feature[] writerFeatures = new JSONWriter.Feature[]{
                JSONWriter.Feature.WriteNulls
                , JSONWriter.Feature.WriteMapNullValue
                , JSONWriter.Feature.WriteNullListAsEmpty
                , JSONWriter.Feature.WriteBigDecimalAsPlain
                , JSONWriter.Feature.WriteLongAsString
        };
        JSONReader.Feature[] readerFeatures = new JSONReader.Feature[]{

        };
        JSON.config(writerFeatures);
        JSON.config(readerFeatures);
    }

    /**
     * fastjson2配置类的Bean
     *
     * @return com.alibaba.fastjson2.support.config.FastJsonConfig fastjson2配置类的Bean
     * @author Telechow
     * @since 2023/3/21 11:05
     */
    @Bean
    public FastJsonConfig fastJsonConfig() {
        FastJsonConfig config = new FastJsonConfig();
        //1.字符集
        config.setCharset(StandardCharsets.UTF_8);

        //2.时间日期格式化，不需要额外配置，默认为"yyyy-MM-dd HH:mm:ss"，如果需要特别处理或者是LocalDate、LocalTime类型，则使用@JsonField注解进行特殊处理

        //3.序列化与反序列化特性
        //3.1.序列化输出空值字段、序列化BigDecimal为字符串、序列化Long为字符串、格式化序列化数据
        config.setWriterFeatures(JSONWriter.Feature.WriteNulls
                , JSONWriter.Feature.WriteMapNullValue
                , JSONWriter.Feature.WriteNullListAsEmpty
                , JSONWriter.Feature.WriteBigDecimalAsPlain
                , JSONWriter.Feature.WriteLongAsString);
        //3.2.不需要额外配置反序列化特性，使用默认的即可
        return config;
    }
}
