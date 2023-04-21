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
