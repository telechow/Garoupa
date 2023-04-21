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
package io.github.telechow.garoupa.web.auto.service.impl;

import io.github.telechow.garoupa.api.entity.AuditLog;
import io.github.telechow.garoupa.web.mapper.AuditLogMapper;
import io.github.telechow.garoupa.web.auto.service.IAuditLogAutoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审计日志表 服务实现类
 * </p>
 *
 * @author telechow
 * @since 2023-04-19
 */
@Service
public class AuditLogAutoServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements IAuditLogAutoService {

}
