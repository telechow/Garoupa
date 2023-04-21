# CosId配置（分布式id）

---



## 分布式id的选型

分布式id现在有很多解决方案，比如：基于redis的自增命令的分布式id、UUID、雪花算法id、号段算法id等等。

基于一些中间件的分布式id，需要引入额外中间件，使用起来不是很友好，这里不再赘述

UUID生成的id是字符串，占的空间比数字id大很多，且它是无序的，插入数据时会频繁对索引进行重构，性能很不好。

现在很多项目使用雪花算法作为分布式id，因为它不需要额外引入中间件、id顺序单调递增、id中可以解析出生成时间、配置简单。

但是雪花算法有个缺点，它对需要用到`BitSet`数据结构的相关算法很不友好。如果项目的并发量不是非常高，那么雪花算法生成的id连续性很差（也就是说有很多id数字被浪费）；此时使用了`BitSet`数据结构的相关算法就会有大量空间被浪费，体现不出其节省空间的特性。

所以我们使用号段算法来作为分布式id的生成算法。加入预取号段的逻辑，来消除号段算法在一个号段的id用尽时，取新号段的性能抖动问题。

 

## CosId简单介绍

`CosId`是一个分布式id解决方案的项目。他支持号段算法，雪花算法；详细的说明请参考[CosId项目的Github](https://github.com/Ahoo-Wang/CosId)



## 使用CosId的号段算法

1. 引入依赖
   
   三个依赖一个是CosId的starter，一个是将号段数据存储在数据库，一个是和mybatis整合
   
   ```xml
   <!--cosid-->
   <dependency>
     <groupId>me.ahoo.cosid</groupId>
     <artifactId>cosid-spring-boot-starter</artifactId>
     <version>${cosid.version}</version>
   </dependency>
   <dependency>
     <groupId>me.ahoo.cosid</groupId>
     <artifactId>cosid-jdbc</artifactId>
     <version>${cosid.version}</version>
   </dependency>
   <dependency>
     <groupId>me.ahoo.cosid</groupId>
     <artifactId>cosid-mybatis</artifactId>
     <version>${cosid.version}</version>
   </dependency>
   ```

2. 配置yaml
   
   ```yaml
   #cosid配置
   cosid:
     namespace: Garoupa
     #使用号段模式，默认链式
     segment:
       enabled: true
       #号段分发器使用jdbc，将号段信息存储在数据库中，cosid号段信息即使在数据库中性能和存在redis中几乎无差别且极高
       distributor:
         type: jdbc
       share:
         #起始id偏移量，配置1000则id从1001开始
         offset: 1000
         #号段步长，每次取号段，号段中的id数量。java服务停服，取到的号段中未使用的id将会被废弃，所以要配置一个合适的步长
         step: 100
       #自定义的号段生产者，我们使用实体名作为号段链生产者的名字
       provider:
         User:
           offset: 1000
           step: 1
         Role:
           offset: 1000
           step: 1
         UserRoleRelation:
           offset: 1000
           step: 1
         Resource:
           offset: 1000
           step: 1
         RoleResourceRelation:
           offset: 1000
           step: 1
         Permission:
           offset: 1000
           step: 1
         RolePermissionRelation:
           offset: 1000
           step: 1
   ```

3. 整合mybatis，在入库时自动生成id
   
   在实体类中id字段加入`@CosId(value = "User")`注解，其中`value`的值设置为实体名，和yaml中的配置对应
   
   `mybaits`的`@TableId(value = "id", type = IdType.INPUT)`注解，`type`的值设置为`IdType.INPUT`该类型可以通过自己注册自动填充插件进行填充，此时我们注册的自动填充插件就是`cosid-mybatis`
   
   ```java
   @CosId(value = "User")
   @TableId(value = "id", type = IdType.INPUT)
   private Long id;
   ```


