package io.github.jqdi.i18n.core.annotation;

import java.lang.annotation.*;

/**
 * 国际化字段注解
 *
 * @author JQ棣
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nField {
    /**
     * 国际化字段名（数据库字段名）
     */
    String i18nColumn();
}
