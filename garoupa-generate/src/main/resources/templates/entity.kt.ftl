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
package ${package.Entity}

<#list table.importPackages as pkg>
import ${pkg}
</#list>
<#if springdoc>
import io.swagger.v3.oas.annotations.media.Schema;
<#elseif swagger>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>

/**
 * <p>
 * ${table.comment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if table.convert>
@TableName("${schemaName}${table.name}")
</#if>
<#if springdoc>
@Schema(name = "${entity}", description = "$!{table.comment}")
<#elseif swagger>
@ApiModel(value = "${entity}对象", description = "${table.comment!}")
</#if>
<#if superEntityClass??>
class ${entity} : ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
class ${entity} : Model<${entity}>() {
<#elseif entitySerialVersionUID>
class ${entity} : Serializable {
<#else>
class ${entity} {
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
    @TableId(value = "${field.annotationColumnName}", type = IdType.AUTO)
<#elseif idType ??>
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
    <#if field.propertyType == "Integer">
    var ${field.propertyName}: Int? = null
    <#else>
    var ${field.propertyName}: ${field.propertyType}? = null
    </#if>

</#list>
<#-- ----------  END 字段循环遍历  ---------->
<#if entityColumnConstant>
    companion object {
<#list table.fields as field>

        const val ${field.name?upper_case} : String = "${field.name}"

</#list>
    }

</#if>
<#if activeRecord>
    override fun pkVal(): Serializable? {
<#if keyPropertyName??>
        return ${keyPropertyName}
<#else>
        return null
</#if>
    }

</#if>
    override fun toString(): String {
        return "${entity}{" +
<#list table.fields as field>
<#if field_index==0>
        "${field.propertyName}=" + ${field.propertyName} +
<#else>
        ", ${field.propertyName}=" + ${field.propertyName} +
</#if>
</#list>
        "}"
    }
}
