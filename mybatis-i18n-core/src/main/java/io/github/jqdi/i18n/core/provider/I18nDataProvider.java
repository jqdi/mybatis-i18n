package io.github.jqdi.i18n.core.provider;

import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.I18nTableInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;

/**
 * 国际化数据提供者
 *
 * @author JQ棣
 */
public interface I18nDataProvider {
    /**
     * 获取 关联字段-国际化字段 值映射
     *
     * @param i18nTableInfo 国际化表信息
     * @param relatedFieldValueSet 关联字段值列表（去重）
     * @return 按I18nFieldInfo维度的 关联字段-国际化字段 值映射
     */
    Map<I18nFieldInfo, List<RelatedI18nValueMapping>> getValueMapping(I18nTableInfo i18nTableInfo,
        Set<Object> relatedFieldValueSet);
}
