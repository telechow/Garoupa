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
