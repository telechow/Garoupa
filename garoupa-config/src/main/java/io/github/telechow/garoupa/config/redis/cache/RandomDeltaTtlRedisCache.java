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
