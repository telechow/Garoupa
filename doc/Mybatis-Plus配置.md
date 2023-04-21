# Mybatis-Plus配置

---

## 引入依赖

```xml
<properties>
    <!--mybatis-plus-->
    <mybatis-plus.version>3.5.3.1</mybatis-plus.version>
</properties>

<dependencies>
    <!--mybatis-plus-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>${mybatis-plus.version}</version>
    </dependency>
</dependencies>
```

## 包扫描配置

```java
package io.github.telechow.garoupa.config.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 *
 * @author Telechow
 * @since 2023/3/26 15:40
 */
@Configuration
@MapperScan("io.github.telechow.garoupa.web.mapper")
public class MybatisPlusConfiguration {

}
```

## 插件配置

```java
package io.github.telechow.garoupa.config.mybatis.plus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 *
 * @author Telechow
 * @since 2023/3/26 15:40
 */
@Configuration
@MapperScan("io.github.telechow.garoupa.web.mapper")
public class MybatisPlusConfiguration {

    /**
     * Mybatis-Plus插件拦截器的Bean
     *
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor Mybatis-Plus插件拦截器的Bean
     * @author Telechow
     * @since 2023/3/26 15:47
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //防止全表更新或删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}
```

## 字段填充配置

1. 我们在插入或更新时，希望一些字段能够自动填充，减少我们的开发量。Garoupa脚手架中`createUser`,`createTime`,`deleteTag`字段需要在插入是填充，`updateUser`,`updateTime`需要在插入和更新时填充。

2. 用户id从`SpringSecurity`的上下文中或获取，如果获取不到就填充-1；时间则取当前系统时间；逻辑删除标识在插入时自然是填充0

3. `Mybatis-Plus`的`MetaObjectHandler`接口中填充方法的默认策略都是：如果属性有值则不覆盖,如果填充值为`null`则不填充；我们重写`strictFillStrategy`方法，将其改为如果属性有值覆盖，如果填充值为`null`也要填充

```java
package io.github.telechow.garoupa.config.mybatis.plus.meta.object.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Garoupa自动填充字段处理
 *
 * @author Telechow
 * @since 2023/3/26 15:53
 */
@Component
public class GaroupaMetaObjectHandle implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        final Long loginUserId = this.getLoginUserId();
        final LocalDateTime now = LocalDateTime.now();
        this.strictInsertFill(metaObject, "createUser", () -> loginUserId, Long.class);
        this.strictInsertFill(metaObject, "createTime", () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateUser", () -> loginUserId, Long.class);
        this.strictInsertFill(metaObject, "updateTime", () -> now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "deletedTag", () -> 0L, Long.class);
    }

    /**
     * 注意只对一下方法生效：<br>
     * updateById(T entity)方法<br>
     * update(T entity, Wrapper<T> updateWrapper)方法，且entity不能为null<br>
     * 对update(Wrapper<T> updateWrapper)不生效
     *
     * @param metaObject 元对象
     * @author Telechow
     * @since 2023/3/26 17:08
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateUser", this::getLoginUserId, Long.class);
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        //修改严格模式填充策略,设置为有值覆盖,如果提供的值为null也要填充
        metaObject.setValue(fieldName, fieldVal.get());
        return this;
    }

    /// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 私有方法 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 获取当前登录用户id，如果获取不到返回-1
     *
     * @return java.lang.Long 成功返回当前登录用户id，失败返回-1
     * @author Telechow
     * @since 2023/3/26 15:58
     */
    private Long getLoginUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication)){
            return -1L;
        }

        Object principal = authentication.getPrincipal();
        if(Objects.isNull(principal)){
            return -1L;
        }

        if(principal instanceof Long){
            return (Long)principal;
        }
        return -1L;
    }
}
```

## 逻辑删除配置（同时解决逻辑删除和唯一性冲突问题）

mybatis-plus提供了逻辑删除的功能。但是很多时候，我们的表中需要逻辑删除和唯一键一起使用。官网中的`0 or 1`的方式，无法很好的解决这两个需求的冲突。

我们使用逻辑删除字段，0表示未删除，和此条数据相同的id值表示删除。这样，使用`(唯一字段,逻辑删除字段)`作为唯一键，就可以同时满足逻辑删除和唯一键的需求。

```yaml
#Mybatis-plus配置
mybatis-plus:
  global-config:
    #关闭banner
    banner: false
    db-config:
      #解决逻辑删除与唯一性校验的冲突问题，使用被删除的数据id填充逻辑删除字段，以唯一字段+逻辑删除字段作为组合唯一键
      logic-delete-value: "id"
      logic-not-delete-value: 0
```
