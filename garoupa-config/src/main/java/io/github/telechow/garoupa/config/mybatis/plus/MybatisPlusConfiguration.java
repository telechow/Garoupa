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
package io.github.telechow.garoupa.config.mybatis.plus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis-Plus配置
 *
 * @author Telechow
 * @since 2023/3/26 15:40
 */
@Configuration
@MapperScan("io.github.telechow.garoupa.web.mapper")
public class MybatisPlusConfiguration {

	/**
	 * Mybatis-Plus插件拦截器的Bean
	 *
	 * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor Mybatis-Plus插件拦截器的Bean
	 * @author Telechow
	 * @since 2023/3/26 15:47
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		//防止全表更新或删除插件
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		//乐观锁插件
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		//分页插件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
		return interceptor;
	}

}
