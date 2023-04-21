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
            "garoupa_resource",
            "garoupa_system_param_category",
            "garoupa_system_param",
            "garoupa_system_dict",
            "garoupa_system_dict_item",
            "garoupa_login_log",
            "garoupa_audit_log"
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
                        .pathInfo(Collections.singletonMap(OutputFile.xml, "D://Garoupa/Mapper"))// 设置mapperXml生成路径
                        .controller("web.controller")
                        .entity("api.entity")
                        .mapper("web.mapper")
                        .service("web.auto.service")
                        .serviceImpl("web.auto.service.impl")
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
                        .serviceBuilder().formatServiceFileName("I%sAutoService").formatServiceImplFileName("%sAutoServiceImpl").enableFileOverride()
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
