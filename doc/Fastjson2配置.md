# Fastjson2配置

---

## 引入依赖

```xml
<properties>
    <!--fastjson2-->
    <fastjson2.version>2.0.26</fastjson2.version>
</properties>

<dependencies>
    <!--fastjson2-->
    <dependency>
        <groupId>com.alibaba.fastjson2</groupId>
        <artifactId>fastjson2-extension-spring6</artifactId>
        <version>${fastjson2.version}</version>
    </dependency>
</dependencies>
```

## fastjson2配置Bean注入

```java
package io.github.telechow.garoupa.config.fastjson2;

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
        //3.1.序列化输出空值字段、序列化BigDecimal为字符串、序列化Long为字符串
        config.setWriterFeatures(JSONWriter.Feature.WriteNulls
            , JSONWriter.Feature.WriteMapNullValue
            , JSONWriter.Feature.WriteNullListAsEmpty
            , JSONWriter.Feature.WriteBigDecimalAsPlain
            , JSONWriter.Feature.WriteLongAsString);
        //3.2.不需要额外配置反序列化特性，使用默认的即可
        return config;
    }
}
```

其中序列化与反序列化特性，可以参考[fastjson2官方github仓库](https://github.com/alibaba/fastjson2/blob/main/docs/features_cn.md)

同时此处配置也解决了BigDecimal和Long的JavaScript精度丢失问题

## fastjson2与SpringMVC(SpringBoot)整合

```java
package io.github.telechow.garoupa.config.fastjson2.web.mvc.configurer;

import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * 使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter 以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。
 * 
 * @author Telechow
 * @since 2023/3/21 11:26
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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
}
```

这样使用的就是fastjson2来对SpringMVC进行序列化与反序列化了

LocalDateTime、LocalDate、LocalTime的序列化需反序列化是默认使用"yyyy-MM-dd HH:mm:ss"来进行的，如果有特殊需要，可以使用@JsonField注解自行自定义

## fastjson2与openapi3.x冲突的解决配置

具体可以查看[Fastjson2的issue1256](https://github.com/alibaba/fastjson2/issues/1256)，有本人的~~精彩回答~~（不要脸的自我表扬）截至此文章编写之时，温少已经推出了`2.0.27-SNAPSHOT`版本修复了此问题，之后版本应该不需要此修复。bug修复的方法和我在issue中的回答，第二种解法类似（与此处的解法不同）

我们先看一下`FastJsonHttpMessageConverter`的一段源码

```java
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        HttpHeaders headers = outputMessage.getHeaders();

        try {
            int contentLength;
            if (object instanceof String && JSON.isValidObject((String) object)) {
                byte[] strBytes = ((String) object).getBytes(config.getCharset());
                contentLength = strBytes.length;
                outputMessage.getBody().write(strBytes, 0, strBytes.length);
            } else {
                contentLength = JSON.writeTo(
                        outputMessage.getBody(),
                        object, config.getDateFormat(),
                        config.getWriterFilters(),
                        config.getWriterFeatures()
                );
            }

            if (headers.getContentLength() < 0 && config.isWriteContentLength()) {
                headers.setContentLength(contentLength);
            }
        } catch (JSONException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new HttpMessageNotWritableException("I/O error while writing output message", ex);
        }
    }
```

可以发现`FastJsonHttpMessageConverter`对响应数据是`byte[]`类型的数据没有像`String`类型那样正确的处理。

然而Openapi的api-docs的控制器确是，响应的`byte[]`

```java
    /**
     * Openapi json string.
     *
     * @param request the request
     * @param apiDocsUrl the api docs url
     * @param locale the locale
     * @return the string
     * @throws JsonProcessingException the json processing exception
     */
    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public byte[] openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl, Locale locale)
            throws JsonProcessingException {
        return super.openapiJson(request, apiDocsUrl, locale);
    }
```

`FastJsonHttpMessageConverter`是无法处理的，所以应该使用Spring自带的`ByteArrayHttpMessageConverter`去处理这样类型的响应。

所以我们需要把`FastJsonHttpMessageConverter`配置在`ByteArrayHttpMessageConverter`之后，在其他转换器之前，且不能在`fastjson2`的配置类使用`@EnableWebMvc`注解去清空掉Spring默认的转换器。故我们需要改造`fastjson2`的配置。

```java
package io.github.telechow.garoupa.config.fastjson2.web.mvc.configurer;

import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.alibaba.fastjson2.support.spring6.webservlet.view.FastJsonJsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Fastjson2整合SpringMVC
 * <li>使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter 以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。</li>
 * <li>使用 FastJsonJsonView 来设置 Spring MVC 默认的视图模型解析器，以提高 @Controller @ResponseBody ModelAndView JSON序列化速度。</li>
 *
 * @author Telechow
 * @since 2023/3/21 11:26
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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

        Integer index = null;
        for (HttpMessageConverter<?> httpMessageConverter : converters) {
            if (httpMessageConverter instanceof ByteArrayHttpMessageConverter) {
                index = converters.indexOf(httpMessageConverter);
                break;
            }
        }
        if (Objects.isNull(index)) {
            converters.add(0, converter);
        } else {
            converters.add(index + 1, converter);
        }
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
        //1.使用io.github.telechow.garoupa.config.fastjson2.Fastjson2Configuration中的配置
        fastJsonJsonView.setFastJsonConfig(fastJsonConfig);
        registry.enableContentNegotiation(fastJsonJsonView);
    }
}
```

重点就是找到`ByteArrayHttpMessageConverter`在`converters`中的位置，并保证将`FastJsonHttpMessageConverter`放在它之后；且保证不使用`@EnableWebMvc`注解，确保

`ByteArrayHttpMessageConverter`不会被清空。
