package io.github.jqdi.i18n.core.annotation;

import java.lang.annotation.*;

import io.github.jqdi.i18n.core.provider.I18nDataProvider;

/**
 * 国际化表注解（类级别）
 * 
 * @author JQ棣
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface I18nTable {
    /**
     * 国际化表名
     */
    String i18nTable();

    /**
     * 关联字段名（数据库字段名）
     */
    String i18nRelatedColumn();

    /**
     * 关联值来源属性名（当前实体类一定会有该属性名，一般是主键）
     */
    String relatedValueFromField();

    /**
     * 国际化语言编码字段名（存储zh-CN、zh-TW、en-US等语言编码）
     */
    String i18nLocaleColumn() default "locale";

    /**
     * 国际化数据源
     */
    Class<? extends I18nDataProvider> i18nDataProvider() default I18nDataProvider.class;
}