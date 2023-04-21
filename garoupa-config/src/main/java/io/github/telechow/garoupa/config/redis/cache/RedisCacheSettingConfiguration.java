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
