# Lock4j分布式锁配置

---

## 引入依赖

```xml
<properties>
    <!--lock4j-->
    <lock4j.version>2.2.4</lock4j.version>
</properties>

<dependencies>
    <!--lock4j-->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>lock4j-redisson-spring-boot-starter</artifactId>
        <version>${lock4j.version}</version>
    </dependency>
</dependencies>
```

## 配置锁获取失败策略

```java
package io.github.telechow.garoupa.config.lock4j;

import com.baomidou.lock.LockFailureStrategy;
import com.baomidou.lock.exception.LockException;
import io.github.telechow.garoupa.api.enums.ResponseCode;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Garoupa的分布式锁获取失败策略
 *
 * @author Telechow
 * @since 2023/4/6 16:01
 */
@Component
public class GaroupaLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        throw new LockException(ResponseCode.ON_LOCK_FAIL.getMsg());
    }
}
```

## 使用分布式锁

我们使用redisson实现的分布式锁。用法如下，在需要分布式锁的方法上加入注解：

```java
@Lock4j(keys = "T(io.github.telechow.garoupa.web.utils.AuthenticationUtil).getUserId()")
```

keys可以使用SPEL，这里是获取了当前登录用户id。

其他详细的用法参考[Lock4j的Github](https://github.com/baomidou/lock4j)
