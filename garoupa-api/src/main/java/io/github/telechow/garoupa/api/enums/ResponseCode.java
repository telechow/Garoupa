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
package io.github.telechow.garoupa.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author Telechow
 * @since 2023/3/27 20:51
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

    /// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ A类-本系统错误 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    ///############################################################ 通用错误A00

    /**
     * 无错误，一切正常
     */
    SUCCESS("A0000", "ok"),

    /**
     * 访问失败，此响应码为本系统的总响应失败，一般不使用
     */
    FAIL("A0001", "访问失败"),

    /// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ A类-本系统错误 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ B类-客户端错误 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    ///############################################################ 客户端错误大类相关错误，B00

    /**
     * 认证（登录）失败，总的错误，使用时尽可能覆盖掉此错误消息，提示用户详细一点
     */
    AUTHENTICATION_FAILURE("B0000", "登录失败"),

    /**
     * 授权失败，总的错误，使用时尽可能覆盖掉此错误消息，提示用户详细一点
     */
    ACCESS_DENIED("B0001", "无权访问"),

    /**
     * 请求参数错误
     */
    REQUEST_PARAMETER_ERROR("B0002", "请求参数错误"),

    /**
     * 两次输入的新密码不一致
     */
    TWO_NEO_PASSWORD_NOT_MATCH("B0003", "两次输入的新密码不一致"),

    /**
     * 原始密码输入错误
     */
    ORIGIN_PASSWORD_NOT_MATCH("B0004", "原始密码输入错误"),

    /**
     * 密码不满足要求
     */
    NEO_PASSWORD_NOT_SATISFY_REQUIREMENT("B0005", "密码不满足要求"),

    ///############################################################ 认证相关错误，B01

    /**
     * 认证时用户不存在，提示用户名或密码错误
     */
    AUTHENTICATION_USER_NOT_EXIST("B0101", "用户名或密码错误"),

    /**
     * 认证时用户名和密码不匹配，提示用户名或密码错误
     */
    AUTHENTICATION_FAIL("B0102", "用户名或密码错误"),

    /**
     * 用户名密码验证码认证时，没有数据密码，提示请输入密码
     */
    USERNAME_PASSWORD_CAPTCHA_AUTHENTICATION_NOT_INPUT_PASSWORD("B0103", "请输入密码"),

    /**
     * 用户名密码验证码认证时，密码匹配失败，提示请输入密码
     */
    USERNAME_PASSWORD_CAPTCHA_AUTHENTICATION_PASSWORD_NOT_MATCHES("B0104", "用户名或密码错误"),

    /**
     * 验证码不存在或已过期，提示验证码错误
     */
    AUTHENTICATION_CAPTCHA_NOT_EXIST_OR_EXPIRE("B0105", "验证码错误，请重新验证"),

    /**
     * 验证码错误
     */
    AUTHENTICATION_CAPTCHA_NOT_MATCH("B0106", "验证码错误，请重新验证"),

    /**
     * 认证时邮箱地址不存在，提示邮箱地址或邮件验证码错误
     */
    AUTHENTICATION_EMAIL_NOT_EXIST("B0107", "邮箱地址或邮件验证码错误"),

    /**
     * 邮件验证码错误或已过期
     */
    AUTHENTICATION_EMAIL_CODE_NOT_MATCH_OR_EXPIRE("B0108", "邮件验证码错误或已过期，请重新验证"),

    /**
     * 用户token非法
     */
    AUTHENTICATION_TOKEN_ILLEGAL("B0109", "用户token非法"),

    /**
     * 用户token中解析出的用户id对应的用户实体不存在，提示用户token非法
     */
    AUTHENTICATION_TOKEN_SUBJECT_NOT_EXIST("B0110", "用户token非法"),

    ///############################################################ 授权相关错误，B02

    ///############################################################ 请求参数相关错误，B03

    /**
     * 违反数据库唯一约束条件，提示请求参数的值已经被使用
     */
    VIOLATION_SQL_INTEGRITY_CONSTRAINT("B0301", "请求参数的值已经被使用"),

    /**
     * 请求参数检验错误
     */
    REQUEST_PARAM_VALIDATE_FAIL("B0302", "请求参数检验错误"),

    /**
     * 指定的数据不存在
     */
    THE_SPECIFIED_DATA_NOT_EXIST("B0303", "指定的数据不存在"),

    /**
     * 指定的上级数据不存在
     */
    THE_SPECIFIED_PARENT_DATA_NOT_EXIST("B0304", "指定的上级数据不存在"),

    /**
     * 身份证号校验错误
     */
    ID_CARD_NUMBER_VALIDATE_ERROR("B0305", "身份证号校验错误"),

    /**
     * 电话号码校验错误
     */
    MOBILE_VALIDATE_ERROR("B0306", "电话号码校验错误"),

    /**
     * 电子邮箱校验错误
     */
    EMAIL_VALIDATE_ERROR("B0307","电子邮箱校验错误"),

    ///############################################################ 系统参数相关错误，B04

    /**
     * 系统参数已锁定
     */
    SYSTEM_PARAM_LOCKED("B0401", "系统参数已锁定"),

    /**
     * 系统参数分类已锁定
     */
    SYSTEM_PARAM_CATEGORY_LOCKED("B0402", "系统参数分类已锁定"),

    /**
     * 系统参数解析失败
     */
    SYSTEM_PARAM_PARSING_ERROR("B0403", "系统参数解析失败"),

    /**
     * 系统参数分类包含系统参数，不允许删除
     */
    SYSTEM_PARAM_CATEGORY_NOT_ALLOW_DELETE_WHEN_CONTAIN_SYSTEM_PARAM("B0404", "系统参数分类包含系统参数，不允许删除"),

    ///############################################################ 系统字典相关错误，B05

    /**
     * 系统字典已锁定
     */
    SYSTEM_DICT_LOCKED("B0501", "系统字典已锁定"),

    /**
     * 系统字典包含系统字典项，不允许删除
     */
    SYSTEM_DICT_NOT_ALLOW_DELETE_WHEN_CONTAIN_SYSTEM_DICT_ITEM("B0502", "系统字典包含系统字典项，不允许删除"),

    /**
     * 系统字典项已锁定
     */
    SYSTEM_DICT_ITEM_LOCKED("B0503", "系统字典项已锁定"),

    /**
     * 系统字典项不存在
     */
    SYSTEM_DICT_ITEM_NOT_EXIST("B0504", "系统字典项不存在"),

    ///############################################################ RBAC相关错误，B06

    /**
     * 资源的上级资源必须是菜单
     */
    RBAC_RESOURCE_PARENT_MUST_A_MENU("B0601", "资源的上级资源必须是菜单"),

    /**
     * 资源包含下级资源，不允许删除
     */
    RBAC_RESOURCE_NOT_ALLOW_DELETE_WHEN_CONTAIN_CHILDREN_RESOURCE("B0602", "资源包含下级资源，不允许删除"),

    /**
     * 资源关联了权限，请先删除权限后在删除此资源
     */
    RBAC_RESOURCE_NOT_ALLOW_DELETE_WHEN_ASSOCIATION_PERMISSION("B0603", "资源关联了权限，请先删除权限后在删除此资源"),

    /**
     * 菜单不存在
     */
    RBAC_MENU_RESOURCE_NOT_EXIST("B0604", "菜单不存在"),

    /**
     * 角色存在下级角色，请先删除下级角色
     */
    RBAC_ROLE_NOT_ALLOW_DELETE_WHEN_CONTAIN_CHILDREN_ROLE("B0605", "角色存在下级角色，请先删除下级角色"),

    /**
     * 角色关联了用户，不允许删除
     */
    RBAC_ROLE_NOT_ALLOW_DELETE_WHEN_ASSOCIATION_USER("B0606", "角色关联了用户，不允许删除"),

    /**
     * 角色关联资源超出了上级角色的资源范围
     */
    RBAC_ROLE_ASSOCIATE_RESOURCE_OUT_OF_PARENT_ROLE_SCOPE("B0607", "角色关联资源超出了上级角色的资源范围"),

    /**
     * 资源至少有一个不存在
     */
    RBAC_RESOURCE_AT_LEAST_ONE_NOT_EXIST("B0608", "资源至少有一个不存在"),

    /**
     * 权限至少有一个不存在
     */
    RBAC_PERMISSION_AT_LEAST_ONE_NOT_EXIST("B0609", "权限至少有一个不存在"),

    /**
     * 角色关联权限超出了上级角色的权限范围
     */
    RBAC_ROLE_ASSOCIATE_PERMISSION_OUT_OF_PARENT_ROLE_SCOPE("B0610", "角色关联权限超出了上级角色的权限范围"),

    /**
     * 角色至少有一个不存在
     */
    RBAC_ROLE_AT_LEAST_ONE_NOT_EXIST("B0611", "角色至少有一个不存在"),

    /// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ B类-客户端错误 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ C类-第三方错误 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    ///############################################################ Lock4j相关错误，C01

    /**
     * 获取分布式锁失败，提示服务器正忙，请稍后再试
     */
    ON_LOCK_FAIL("C0100", "服务器正忙，请稍后再试"),

    /// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ C类-第三方错误 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    ;

    private final String code;
    private final String msg;
}
