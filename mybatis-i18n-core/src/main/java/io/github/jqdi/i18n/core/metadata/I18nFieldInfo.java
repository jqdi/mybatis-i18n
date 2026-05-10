package io.github.jqdi.i18n.core.metadata;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 国际化字段信息（实体类字段与数据库的字段对应关系）
 *
 * @author JQ棣
 */
public class I18nFieldInfo {
    /**
     * 本字段属性
     */
    private final Field field;

    /**
     * 国际化字段名
     */
    private final String i18nColumn;

    public I18nFieldInfo(Field field, String i18nColumn) {
        this.field = field;
        this.i18nColumn = i18nColumn;
    }

    public Field getField() {
        return field;
    }

    public String getI18nColumn() {
        return i18nColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        I18nFieldInfo that = (I18nFieldInfo)o;
        return Objects.equals(field, that.field) && Objects.equals(i18nColumn, that.i18nColumn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, i18nColumn);
    }
}
