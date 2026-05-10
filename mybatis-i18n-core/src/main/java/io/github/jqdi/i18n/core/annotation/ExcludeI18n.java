package io.github.jqdi.i18n.core.annotation;

import java.lang.annotation.*;

/**
 * 排除国际化注解
 *
 * @author JQ棣
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcludeI18n {}
