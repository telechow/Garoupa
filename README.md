<p align="center"><font align="center" size=12>GAROUPA</font></p>

<p align="center">
    <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0">
        <img alt="License" src="https://img.shields.io/badge/License-Apache%202-brightgreen" />
    </a>
    <a target="_blank" href="https://openjdk.org/">
        <img alt="JDK version" src="https://img.shields.io/badge/JDK-17-brightgreen" />
    </a>
    <a target="_blank" href='https://github.com/telechow/Garoupa'>
        <img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/telechow/Garoupa?style=social">
    </a>
</p>

---

# 简要说明

`Garoupa`意为`石斑鱼`，是作者我最喜欢吃的鱼类。也是我人生中的第一个稍微像样的开源项目。写的不好，多多包涵。用于一些小型的项目绰绰有余。

本项目只是一个Java后端项目，不含前端代码（因为不会前端）。

此项目是基于`SpringBoot 3.x`和`JDK 17`开发的单体架构的框架。

---

# 快速开始

1. 克隆代码

```shell
git clone https://github.com/telechow/Garoupa.git
```

2. 修改yaml文件，将数据库、redis、等等中间件的地址

```yaml
spring:
  #数据源
  datasource:
    url: jdbc:mysql://ip:port/garoupa?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=GMT%2B8
    username: your_username
    password: your_password
  #redis
  data:
    redis:
      host: ip
      port: port
      database: 0
      password: your_password
  redis:
    redisson:
      config: |
        singleServerConfig:
          address: "redis://ip:port"
          password: your_password
          database: 0
          idleConnectionTimeout: 10000
          connectTimeout: 10000
          timeout: 3000
          retryAttempts: 3
          retryInterval: 1500
          subscriptionsPerConnection: 5
          clientName: null
          subscriptionConnectionMinimumIdleSize: 1
          subscriptionConnectionPoolSize: 50
          connectionMinimumIdleSize: 24
          connectionPoolSize: 64
          dnsMonitoringInterval: 5000
        threads: 16
        nettyThreads: 32
        codec: !<org.redisson.codec.Kryo5Codec> {}
        transportMode: "NIO"
  #邮箱配置
  mail:
    host: smtp.qq.com #发送邮件服务器
    username: your_email #QQ邮箱
    password: your_password #客户端授权码
    protocol: smtp #发送邮件协议
    default-encoding: utf-8
```

3. 运行项目

---

# 核心依赖

| 依赖             | 版本      |
| -------------- |:-------:|
| JDK            | 17      |
| SpringBoot     | 3.0.5   |
| SpringSecurity | 6.0.2   |
| Mybatis-plus   | 3.5.3.1 |
| lock4j         | 2.2.4   |
| cosid          | 2.0.5   |
| redisson       | 3.20.1  |
| knife4j        | 4.1.0   |
| Hutool         | 5.8.18  |

---

# 功能列表

- [x] RBAC用户角色权限资源管理        
- [x] SpringSecurity认证鉴权及安全配置
- [x] 系统参数
- [x] 系统字典
- [x] 登录日志、审计日志
- [x] 接口文档
- [x] 分布式锁
- [x] 全局异常处理

--- 

# 文档

待完善...

---

# 开源协议

开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。 允许商业使用，但务必保留类作者、Copyright 信息。

---

# 其他说明

1. 欢迎提交 [issue](https://github.com/telechow/Garoupa/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

2. 联系作者 `laughho@qq.com`，除了软件开发之外，作者还是个酿酒爱好者。欢迎交流学习。