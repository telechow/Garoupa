# Redis配置

---

## 引入依赖

我们使用redisson作为redis的客户端，因为redisson使用方便、序列化与反序列化自定义方便、已经实现好的方便的数据结构，如布隆过滤器、批量执行、事务。实在是太优秀了。

```xml
<properties>
    <!--redisson-->
    <redisson.version>3.20.1</redisson.version>
</properties>

<dependencies>
    <!--redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
    </dependency> 
    <!--redisson-->
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson-spring-boot-starter</artifactId>
        <version>${redisson.version}</version>
    </dependency> 
</dependencies>
```

## redis使用fastjson2进行序列化与反序列化

```java
package io.github.telechow.garoupa.config.redis;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis配置
 *
 * @author Telechow
 * @since 2023/3/21 16:08
 */
@Configuration
public class RedisConfiguration {

    /**
     * RedisTemplate<Object, Object>的Bean
     * <li>使用fastjson2进行序列化与反序列化</li>
     *
     * @param redisConnectionFactory redis连接工厂
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String,java.lang.Object> RedisTemplate<Object, Object>的Bean
     * @author Telechow
     * @since 2023/3/21 16:21
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置默认的Serialize，包含 keySerializer & valueSerializer
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);
        return redisTemplate;
    }
}
```

## RedisCache配置缓存过期时间带随机数改造，防止缓存雪崩问题

继承`RedisCache`类，重写`put`、`putIfAbsent`方法，如果过期时间ttl存在且大于0，则在ttl加上一个随机的时间作为新的ttl，并传入`cacheWriter.put`

```java
package io.github.telechow.garoupa.config.redis.cache;

import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机增加过期时间redis缓存类
 * <li>用于防止缓存雪崩</li>
 *
 * @author Telechow
 * @since 2023/3/21 18:41
 */
public class RandomDeltaTtlRedisCache extends RedisCache {

    private final long originMilli;

    private final long boundMilli;

    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    private final String name;

    private final RedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration cacheConfig;

    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected RandomDeltaTtlRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig
        , long originMilli, long boundMilli) {
        super(name, cacheWriter, cacheConfig);
        if (originMilli < 0) {
            throw new IllegalArgumentException("originMilli should be bigger than or equal 0");
        }
        if (originMilli > boundMilli) {
            throw new IllegalArgumentException("boundMilli should be bigger than minSecond");
        }
        this.originMilli = originMilli;
        this.boundMilli = boundMilli;
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.cacheConfig = cacheConfig;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            throw new IllegalArgumentException(String.format(
                "Cache '%s' does not allow 'null' values; Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration",
                name));
        }

        if (cacheConfig.getTtl() != null && !cacheConfig.getTtl().isZero() && !cacheConfig.getTtl().isNegative()) {
            Duration randomDeltaTtl = cacheConfig.getTtl().plusMillis(random.nextLong(originMilli, boundMilli));
            cacheWriter.put(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue), randomDeltaTtl);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        Object cacheValue = preProcessCacheValue(value);

        if (!isAllowNullValues() && cacheValue == null) {
            return get(key);
        }

        Duration randomDeltaTtl;
        if (cacheConfig.getTtl() != null && !cacheConfig.getTtl().isZero() && !cacheConfig.getTtl().isNegative()) {
            randomDeltaTtl = cacheConfig.getTtl().plusMillis(random.nextLong(originMilli, boundMilli));
        }else {
            randomDeltaTtl = cacheConfig.getTtl();
        }

        byte[] result = cacheWriter.putIfAbsent(name, createAndConvertCacheKey(key), serializeCacheValue(cacheValue),
            randomDeltaTtl);

        if (result == null) {
            return null;
        }

        return new SimpleValueWrapper(fromStoreValue(deserializeCacheValue(result)));
    }

    /**
     * 创建并转换缓存键
     *
     * @param key 缓存键
     * @return byte[] 缓存键字节数组
     * @author Telechow
     * @since 2023/3/21 21:52
     */
    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }
}
```

有了`RandomDeltaTtlRedisCache`之后，如何让它代替原本的`RedisCache`呢？  
在`RedisCacheManager`中，所有的`Cache`都是由一个`createRedisCache`方法生成的

```java
protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
    return new RedisCache(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
}
```

因此只要继承`RedisCacheManger`并重写这个方法就好了

```java
package io.github.telechow.garoupa.config.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * 随机增量过期时间redis缓存管理器
 *
 * @author Telechow
 * @since 2023/3/21 18:47
 */
@Slf4j
public class RandomDeltaTtlRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration defaultCacheConfig;

    private final long originMilli;

    private final long boundMilli;

    public RandomDeltaTtlRedisCacheManager(RedisCacheWriter cacheWriter
        , RedisCacheConfiguration defaultCacheConfiguration, long originMilli, long boundMilli) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.originMilli = originMilli;
        this.boundMilli = boundMilli;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new RandomDeltaTtlRedisCache(name, cacheWriter
            , cacheConfig != null ? cacheConfig : defaultCacheConfig, originMilli, boundMilli);
    }
}
```

之后只要注入自定义的`RandomDeltaTtlRedisCacheManager`作为`CacheManager`就好了。

我们使用`@EnableCaching`开启redis缓存

配置了默认缓存30分钟过期，并使用fastjson2序列化与反序列化缓存值

```java
package io.github.telechow.garoupa.config.redis.cache;

import com.alibaba.fastjson2.support.spring6.data.redis.GenericFastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * redis缓存配置
 *
 * @author Telechow
 * @since 2023/3/21 19:18
 */
@EnableCaching
@Configuration
public class RedisCacheSettingConfiguration {

    /**
     * 默认缓存的过期时间，30分钟
     */
    public static final Duration DEFAULT_CACHE_TTL = Duration.ofMinutes(30L);

    /**
     * 缓存管理器的Bean
     * <li>配置多种缓存配置</li>
     * <li>配置的缓存过期时间，如果ttl>0，则将加上一个随机数，用于防止缓存雪崩</li>
     *
     * @return org.springframework.cache.CacheManager 缓存管理器的Bean
     * @author Telechow
     * @since 2023/3/21 16:48
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        GenericFastJsonRedisSerializer fastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(DEFAULT_CACHE_TTL)
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer));
        return new RandomDeltaTtlRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)
            , redisCacheConfiguration, 0, 60 * 1000);
    }
}
```

最后我们使用`@Cacheable`等注解来操作缓存，即可发现每个ttl大于0的缓存都增加了0到60000毫秒的随机过期时间

## @Cacheable注解中设置缓存基础过期时间

`@Cacheable`注解本来是没有设置缓存过期时间的功能的。我们将其改造一下。使用的时候只要在`value`属性中写成`缓存名称#过期毫秒数的`形式，即可对每个缓存单独设置过期时间，如果不写成此形式，或者#后面写的不是一个数字，则使用默认的缓存过期时间`30分钟`（见上面的代码）

实现上述功能，我们只需要修改`CacheManager.createRedisCache`方法即可；将前面例子中的`RandomDeltaTtlRedisCacheManager`改造一下即可

```java
package io.github.telechow.garoupa.config.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 随机增量过期时间redis缓存管理器
 *
 * @author Telechow
 * @since 2023/3/21 18:47
 */
@Slf4j
public class RandomDeltaTtlRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration defaultCacheConfig;

    private final long originMilli;

    private final long boundMilli;

    public RandomDeltaTtlRedisCacheManager(RedisCacheWriter cacheWriter
        , RedisCacheConfiguration defaultCacheConfiguration, long originMilli, long boundMilli) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
        this.originMilli = originMilli;
        this.boundMilli = boundMilli;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        //1.从缓存名称中获取缓存过期时间，如果不为空则重设缓存过期时间
        Duration ttl = this.getTtlByName(name);
        if (ttl != null) {
            cacheConfig = cacheConfig.entryTtl(ttl);
        }

        //2.创建随机增量过期时间redis缓存
        return new RandomDeltaTtlRedisCache(getCacheName(name), cacheWriter
            , cacheConfig != null ? cacheConfig : defaultCacheConfig, originMilli, boundMilli);
    }

    /// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 私有方法 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    private static final String DEFAULT_SEPARATOR = "#";

    /**
     * 根据缓存名称获取缓存过期时间
     *
     * @param name 缓存名称，其中可能含有过期时间，可能没有过期时间
     * @return java.time.Duration 缓存过期时间
     * @author Telechow
     * @since 2023/3/21 18:53
     */
    private Duration getTtlByName(String name) {
        if (name == null) {
            return null;
        }
        //根据分隔符拆分字符串，并进行过期时间ttl的解析
        String[] cacheParams = name.split(DEFAULT_SEPARATOR);
        if (cacheParams.length > 1) {
            String ttl = cacheParams[1];
            if (StringUtils.hasText(ttl)) {
                try {
                    return Duration.ofMillis(Long.parseLong(ttl));
                } catch (Exception e) {
                    //捕获到异常记录一条警告日志
                    log.warn("缓存名称中的缓存过期时间必须是一个数字", e);
                }
            }
        }
        return null;
    }

    /**
     * 获取真正的缓存名称，去掉缓存时间；如果没有缓存时间则原样返回
     *
     * @param name 可能带缓存时间的缓存名称
     * @return java.lang.String 真正的缓存名称
     * @author Telechow
     * @since 2023/3/21 19:08
     */
    private String getCacheName(String name) {
        String[] cacheParams = name.split(DEFAULT_SEPARATOR);
        if (cacheParams.length > 1) {
            return cacheParams[0];
        }
        return name;
    }
}
```

使用示例：

```java
@GetMapping("/show")
@Cacheable(value = "test#3600000", key = "'IndexController:show:' + #text", unless = "#result == null")
public R<String> show(@RequestParam(name = "text") String text) {
    return R.data("This is index.\nYou say:" + text);
}
```

综合以上两点，我们实现了缓存时间增加一定的随机时间，且可以在缓存注解中设置每个缓存的基础时间，比如上例中。写入缓存后，redis中的ttl应该是3600000至3660000毫秒之间。

## Redisson配置

1. 批量执行命令配置

```java
package io.github.telechow.garoupa.config.redis;

import org.redisson.api.BatchOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * redisson配置
 *
 * @author Telechow
 * @since 2023/3/30 21:22
 */
@Configuration
public class RedissonConfiguration {

    /**
     * redisson批量执行命令设置的Bean
     *
     * @return org.redisson.api.BatchOptions redisson批量执行命令设置的Bean
     * @author Telechow
     * @since 2023/3/30 21:21
     */
    @Bean
    public BatchOptions batchOptions() {
        return BatchOptions.defaults()
                // 指定执行模式，所有命令缓存在Redisson本机内存中统一发送，并以原子性事务的方式执行
                .executionMode(BatchOptions.ExecutionMode.IN_MEMORY_ATOMIC)
                // 将写入操作同步到从节点，同步到2个从节点，等待时间为100毫秒
                .syncSlaves(2, 100, TimeUnit.MILLISECONDS)
                // 处理结果超时为2秒钟
                .responseTimeout(2, TimeUnit.SECONDS)
                // 命令重试等待间隔时间为2秒钟
                .retryInterval(2, TimeUnit.SECONDS)
                // 命令重试次数。仅适用于未发送成功的命令
                .retryAttempts(3);
    }
}
```

2. yaml配置，redis/redisson连接
   
   此处是单节点redis配置，redisson的其他集群方案对应的配置可以参考[Redisson github wiki](https://github.com/redisson/redisson/wiki/2.-Configuration)

```yaml
spring:
  #redis
  data:
    redis:
      host: 192.168.0.200
      port: 6379
      database: 0
      password: 123456
  redis:
    redisson:
      config: |
        singleServerConfig:
          address: "redis://192.168.0.200:6379"
          password: 123456
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
```
