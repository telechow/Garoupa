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
package io.github.telechow.garoupa.web.service;

/**
 * 首页service接口
 *
 * @author Telechow
 * @since 2023/3/27 22:23
 */
public interface IIndexService {

	/**
	 * 展示首页
	 *
	 * @param text 前端说的话
	 * @return java.lang.String 首页的一句话
	 * @author Telechow
	 * @since 2023/1/30 15:27
	 */
	String show(String text);
}
