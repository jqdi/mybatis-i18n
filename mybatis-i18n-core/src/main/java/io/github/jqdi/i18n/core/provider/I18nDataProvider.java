package io.github.jqdi.i18n.core.provider;

import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;

import java.util.List;
import java.util.Set;

/**
 * 国际化数据提供者
 *
 * @author JQ棣
 */
public interface I18nDataProvider {
    /**
     * 获取 关联字段-国际化字段 值映射
     *
     * @param i18nFieldInfo        国际化字段信息
     * @param relatedFieldValueSet 关联字段值列表（去重）
     * @return 关联字段-国际化字段 值映射
     */
    List<RelatedI18nValueMapping> getValueMapping(I18nFieldInfo i18nFieldInfo, Set<Object> relatedFieldValueSet);
}
