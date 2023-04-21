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
