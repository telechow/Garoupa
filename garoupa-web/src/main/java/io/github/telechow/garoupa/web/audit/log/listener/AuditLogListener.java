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
package io.github.telechow.garoupa.web.audit.log.listener;

import io.github.telechow.garoupa.web.audit.log.event.AuditLogEvent;
import io.github.telechow.garoupa.web.auto.service.IAuditLogAutoService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 审计日志监听器
 *
 * @author Telechow
 * @since 2023/4/20 9:29
 */
@AllArgsConstructor
public class AuditLogListener {

    private final IAuditLogAutoService auditLogAutoService;

    /**
     * 异步执行审计日志入库
     *
     * @param auditLogEvent 审计日志事件
     * @author Telechow
     * @since 2023/4/20 9:31
     */
    @Async
    @Order
    @EventListener(AuditLogEvent.class)
    public void saveAuditLog(AuditLogEvent auditLogEvent) {
        auditLogAutoService.save(auditLogEvent.auditLog());
    }
}
