# Mybatis-Plus-Generator代码生成配置

---

## 引入Mybatis-Plus-Generator

```xml
<properties>
    <!--mybatis-plus-->
    <mybatis-plus.version>3.5.3.1</mybatis-plus.version>
    <mybatis-plus-generator.version>3.5.3.1</mybatis-plus-generator.version>

    <!--database-driver-->
    <mysql.version>8.0.32</mysql.version>
</properties>

<dependencies>
    <!--mybatis-plus-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>${mybatis-plus.version}</version>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>${mybatis-plus-generator.version}</version>
    </dependency>
    <!--freemarker模版引擎-->
    <dependency>
        <groupId>org.freemarker</groupId>            
        <artifactId>freemarker</artifactId>
        <scope>compile</scope>
    </dependency>

    <!--database-driver-->
    <!--mysql driver-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>${mysql.version}</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```



## 编写代码生成器类

可以参考注释来看这个类，比较简单，稍微改下数据源和表名就可以使用

我们这里使用了springdoc模式，没有使用swagger模式，即openapi3.x

mybatis-plus自动生成的`service`层，我的习惯是把他作为一个`auto.service`层使用，他并不是我们项目中真正的`service`层。当然每个开发者有自己的理解，可以自行配置，或者干脆不要此层，直接使用`Mapper`

```java
package io.github.telechow.garoupa.generate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * 代码生成器
 *
 * @author Telechow
 * @since 2023/3/21 23:26
 */
public class GaroupaGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
        .Builder("jdbc:mysql://192.168.0.200:3306/garoupa", "root", "123456");

    /**
     * 表名数组，需要自动生成代码的表名写到这里
     */
    private static final String[] TABLE_ARRAY = {
        "garoupa_user",
        "garoupa_user_role_relation",
        "garoupa_role",
        "garoupa_role_permission_relation",
        "garoupa_permission",
        "garoupa_role_resource_relation",
        "garoupa_resource"
    };

    public static void main(String[] args) {
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
            .globalConfig(builder -> {
                builder.author("telechow") // 设置作者
                    .enableSpringdoc() // 开启 springdoc 模式
                    .outputDir("D://Garoupa")// 指定输出目录
                    .commentDate(() -> LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))//注释日期格式
                    .build(); //注释日期;
            })
            .packageConfig(builder -> builder.parent("io.github.telechow.garoupa") // 设置父包名
                .moduleName("web") // 设置父包模块名
                .pathInfo(Collections.singletonMap(OutputFile.xml, "D://Garoupa/Mapper"))// 设置mapperXml生成路径
                .controller("controller")
                .entity("entity")
                .mapper("mapper")
                .service("auto.service")
                .serviceImpl("auto.service.impl")
                .build())
            .strategyConfig(builder -> builder.addInclude(TABLE_ARRAY) // 设置需要生成的表名
                .addTablePrefix("garoupa_")// 设置过滤表前缀
                //实体策略
                .entityBuilder().enableChainModel().enableLombok().logicDeletePropertyName("deletedTag").idType(IdType.INPUT)
                .addTableFills(new Property("createUser", FieldFill.INSERT))
                .addTableFills(new Property("createTime", FieldFill.INSERT))
                .addTableFills(new Property("deletedTag", FieldFill.INSERT))
                .addTableFills(new Property("updateUser", FieldFill.INSERT_UPDATE))
                .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
                .enableFileOverride()
                //mapper策略
                .mapperBuilder().enableBaseColumnList().enableBaseResultMap().enableFileOverride()
                //service策略
                .serviceBuilder().formatServiceFileName("%sAutoService").formatServiceImplFileName("%sAutoServiceImpl").enableFileOverride()
                //controller策略
                .controllerBuilder().enableRestStyle().enableFileOverride()
                .build())
            .templateConfig(builder -> builder
                .controller("/templates/controller.java")
                .entity("/templates/entity.java")
                .mapper("/templates/mapper.java")
                .xml("/templates/mapper.xml")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .build())
            .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .execute();
    }
}
```



## 代码生成模版的相关修改

我们其实只修改了`entity.java.ftl`这个模版文件，其他模版文件只是加了`Apache License 2.0`的文件头罢了。

模版文件放在`resources/templates`文件夹下

主要改造了两点：

1. 在`private static final long serialVersionUID = 1L;`前加上了`@Serial`注解，贴合`java17`的写法。并导入了包。

2. 在主键字段上加了`@CosId(value = "${entity}")`注解，并导入了包。此注解是CosId的Mybatis插件，负责自行生成实体id。至于我们为什么不用mybatis-plus自带的雪花算法id，也不用数据库的自增id，我们之后的章节在进行讨论，[CosId配置（分布式id）](./CosId配置(分布式id).md)

```ftl
package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
import java.io.Serial;
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Getter;
import lombok.Setter;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>
import me.ahoo.cosid.annotation.CosId;

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Getter
@Setter
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if springdoc>
@Schema(name = "${entity}", description = "${table.comment!}")
<#elseif swagger>
@ApiModel(value = "${entity}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#elseif entitySerialVersionUID>
public class ${entity} implements Serializable {
<#else>
public class ${entity} {
</#if>
<#if entitySerialVersionUID>

    @Serial
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if springdoc>
    @Schema(description = "${field.comment}")
        <#elseif swagger>
    @ApiModelProperty("${field.comment}")
        <#else>
    /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if field.keyFlag>
        <#-- 主键 -->
        <#if field.keyIdentityFlag>
    @CosId(value = "${entity}")
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
        <#elseif idType??>
    @CosId(value = "${entity}")
    @TableId(value = "${field.annotationColumnName}", type = IdType.${idType})
        <#elseif field.convert>
    @TableId("${field.annotationColumnName}")
        </#if>
        <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
    @TableField(value = "${field.annotationColumnName}", fill = FieldFill.${field.fill})
        <#else>
    @TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
    @TableField("${field.annotationColumnName}")
    </#if>
    <#-- 乐观锁注解 -->
    <#if field.versionField>
    @Version
    </#if>
    <#-- 逻辑删除注解 -->
    <#if field.logicDeleteField>
    @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>

    public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
    }

    <#if chainModel>
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if chainModel>
        return this;
        </#if>
    }
    </#list>
</#if>
<#if entityColumnConstant>
    <#list table.fields as field>

    public static final String ${field.name?upper_case} = "${field.name}";
    </#list>
</#if>
<#if activeRecord>

    @Override
    public Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }
</#if>
<#if !entityLombokModel>

    @Override
    public String toString() {
        return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
            "${field.propertyName} = " + ${field.propertyName} +
        <#else>
            ", ${field.propertyName} = " + ${field.propertyName} +
        </#if>
    </#list>
        "}";
    }
</#if>
}
```
