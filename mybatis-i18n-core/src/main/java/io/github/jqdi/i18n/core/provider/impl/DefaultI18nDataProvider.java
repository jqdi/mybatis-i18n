package io.github.jqdi.i18n.core.provider.impl;

import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;
import io.github.jqdi.i18n.core.provider.I18nDataProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 国际化数据提供者（默认）
 *
 * @author JQ棣
 */
public class DefaultI18nDataProvider implements I18nDataProvider {

    public DefaultI18nDataProvider() {
    }

    @Override
    public List<RelatedI18nValueMapping> getValueMapping(I18nFieldInfo i18nFieldInfo, Set<Object> relatedFieldValueSet) {
        return Collections.emptyList();
    }
}
