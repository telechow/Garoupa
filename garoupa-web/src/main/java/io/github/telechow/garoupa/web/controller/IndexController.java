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
package io.github.telechow.garoupa.web.controller;

import io.github.telechow.garoupa.api.vo.ResponseResult;
import io.github.telechow.garoupa.web.service.IIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页 控制器
 *
 * @author Telechow
 * @since 2023/1/30 15:24
 */
@RestController
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController {

    private final IIndexService indexService;

    /**
     * 展示首页
     *
     * @param text 前端说的话
     * @return org.springframework.http.ResponseEntity<java.lang.String> 首页的一句话
     * @author Telechow
     * @since 2023/1/30 15:27
     */
    @GetMapping("/show")
    @PreAuthorize("@garoupaExpressionRoot.hasAuthority('super:all:all')")
    public ResponseResult<String> show(@RequestParam(name = "text") String text) {
        return ResponseResult.data(indexService.show(text));
    }
}
