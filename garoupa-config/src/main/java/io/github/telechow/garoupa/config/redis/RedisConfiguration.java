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
	 * @return org.springframework.data.redis.core.RedisTemplate<java.lang.Object, java.lang.Object> RedisTemplate<Object, Object>的Bean
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
