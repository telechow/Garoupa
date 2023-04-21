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
package io.github.telechow.garoupa.config.mybatis.plus.meta.object.handle;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.telechow.garoupa.api.domain.GaroupaUser;
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
    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return -1L;
        }

        Object principal = authentication.getPrincipal();
        if (Objects.isNull(principal)) {
            return -1L;
        }

        if (principal instanceof GaroupaUser) {
            return ((GaroupaUser) principal).getUser().getId();
        }
        return -1L;
    }
}
