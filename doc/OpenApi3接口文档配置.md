# OpenApi3接口文档配置

---

## 引入Knife4j，增强OpenApi3

```xml
<properties>
    <!--knife4j-->
	<knife4j.version>4.1.0</knife4j.version>
</properties>

<dependencies>
    <!--接口文档-->
	<dependency>
		<groupId>com.github.xiaoymin</groupId>
		<artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
		<version>${knife4j.version}</version>
	</dependency>
</dependencies>
```

## 配置OpenApi3.x

java配置类

```java
package io.github.telechow.garoupa.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * 接口文档配置
 *
 * @author Telechow
 * @since 2023/3/24 22:16
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Garoupa接口文档"
    , description = "Garoupa接口文档"
    , version = "0.0.1-SNAPSHOT")
)
public class OpenapiConfiguration {
}
```

yaml文件，注意只在开发环境中开启，生产环境必须将这两个地址关闭

```yaml
# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    enabled: true
    path: /v3/api-docs
  group-configs:
    - group: 'Garoupa'
      paths-to-match: '/**'
      packages-to-scan: io.github.telechow.garoupa
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
```

SpringSecurity放行uri

```
      #Knife4j和OpenApi相关接口
      - /v3/api-docs/**
      - /swagger-resources/**
      - /swagger-ui/**
      - /doc.html
      - /webjars/**
      - /img.icons/**
      - /favicon.ico
```



## 访问接口文档

`http://ip:port/doc.html`



## OpenApi3和OpenApi2的区别

在使用上，也就是注解有所不同，直接参考[官方文档](https://springdoc.org/v2/#migrating-from-springfox)
