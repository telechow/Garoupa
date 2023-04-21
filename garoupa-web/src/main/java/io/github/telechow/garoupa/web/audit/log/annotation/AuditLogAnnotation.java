package io.github.telechow.garoupa.web.audit.log.annotation;

import java.lang.annotation.*;

/**
 * 审计日志注解
 *
 * @author Telechow
 * @since 2023/4/20 8:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLogAnnotation {

    /**
     * 审计日志标题
     *
     * @author Telechow
     * @since 2023/4/20 8:47
     */
    String value() default "";
}
