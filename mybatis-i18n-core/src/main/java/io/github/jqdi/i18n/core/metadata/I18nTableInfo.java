package io.github.jqdi.i18n.core.metadata;

import java.lang.reflect.Field;
import java.util.List;

import io.github.jqdi.i18n.core.provider.I18nDataProvider;

/**
 * 国际化表信息
 *
 * @author JQ棣
 */
public class I18nTableInfo {

    /**
     * 国际化表名
     */
    private final String i18nTable;

    /**
     * 关联字段名
     */
    private final String i18nRelatedColumn;
    /**
     * 关联值来源属性
     */
    private final Field relatedValueFromField;
    /**
     * 国际化语言编码字段名（存储zh-CN、zh-TW、en-US等语言编码）
     */
    private final String i18nLocaleColumn;
    /**
     * 国际化数据提供者
     */
    private final Class<? extends I18nDataProvider> i18nDataProvider;

    /**
     * 本字段属性
     */
    private final List<I18nFieldInfo> i18nFieldInfoList;

    public I18nTableInfo(List<I18nFieldInfo> i18nFieldInfoList, String i18nTable, String i18nRelatedColumn,
        Field relatedValueFromField, String i18nLocaleColumn, Class<? extends I18nDataProvider> i18nDataProvider) {
        this.i18nFieldInfoList = i18nFieldInfoList;
        this.i18nTable = i18nTable;
        this.i18nRelatedColumn = i18nRelatedColumn;
        this.relatedValueFromField = relatedValueFromField;
        this.i18nLocaleColumn = i18nLocaleColumn;
        this.i18nDataProvider = i18nDataProvider;
    }

    public List<I18nFieldInfo> getI18nFieldInfoList() {
        return i18nFieldInfoList;
    }

    public String getI18nTable() {
        return i18nTable;
    }

    public String getI18nRelatedColumn() {
        return i18nRelatedColumn;
    }

    public Field getRelatedValueFromField() {
        return relatedValueFromField;
    }

    public String getI18nLocaleColumn() {
        return i18nLocaleColumn;
    }

    public Class<? extends I18nDataProvider> getI18nDataProvider() {
        return i18nDataProvider;
    }

}
