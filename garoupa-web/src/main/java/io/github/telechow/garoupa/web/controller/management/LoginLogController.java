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
package io.github.telechow.garoupa.web.controller.management;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.telechow.garoupa.api.dto.login.log.PageLoginLogDto;
import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.api.vo.login.log.LoginLogPageVo;
import io.github.telechow.garoupa.web.service.ILoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 登录日志表 前端控制器
 * </p>
 *
 * @author telechow
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/m/login/log")
@Tag(name = "登录日志-控制器", description = "登录日志-控制器")
@RequiredArgsConstructor
public class LoginLogController {

    private final ILoginLogService loginLogService;

    /**
     * 分页查询登录日志
     * <li>按照创建时间降序排序</li>
     *
     * @param dto 分页查询登录日志dto
     * @return io.github.telechow.garoupa.api.vo.ResponseResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < io.github.telechow.garoupa.api.vo.login.log.LoginLogPageVo>> 分页的登录日志分页vo
     * @author Telechow
     * @since 2023/4/18 17:40
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询登录日志"
            , description = "<li>按照创建时间降序排序</li>")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('log:login-log:page')")
    public ResponseResult<Page<LoginLogPageVo>> page(@Validated PageLoginLogDto dto) {
        return ResponseResult.data(loginLogService.page(dto));
    }

}
