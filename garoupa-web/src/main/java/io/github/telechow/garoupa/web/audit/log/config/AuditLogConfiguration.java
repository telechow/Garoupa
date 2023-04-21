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
package io.github.telechow.garoupa.web.audit.log.config;

import io.github.telechow.garoupa.web.audit.log.aspect.AuditLogAspect;
import io.github.telechow.garoupa.web.audit.log.listener.AuditLogListener;
import io.github.telechow.garoupa.web.auto.service.IAuditLogAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 审计日志配置
 *
 * @author Telechow
 * @since 2023/4/20 9:35
 */
@EnableAsync
@Configuration
@RequiredArgsConstructor
public class AuditLogConfiguration {

    private final IAuditLogAutoService auditLogAutoService;

    /**
     * 审计日志监听器的Bean
     *
     * @return io.github.telechow.garoupa.web.audit.log.listener.AuditLogListener 审计日志监听器的Bean
     * @author Telechow
     * @since 2023/4/20 9:34
     */
    @Bean
    public AuditLogListener auditLogListener() {
        return new AuditLogListener(auditLogAutoService);
    }

    /**
     * 审计日志切面处理类的Bean
     *
     * @param publisher 应用事件发布者
     * @return io.github.telechow.garoupa.web.audit.log.aspect.AuditLogAspect 审计日志切面处理类的Bean
     * @author Telechow
     * @since 2023/4/20 9:35
     */
    @Bean
    public AuditLogAspect auditLogAspect(ApplicationEventPublisher publisher) {
        return new AuditLogAspect(publisher);
    }
}
