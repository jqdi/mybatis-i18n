package io.github.jqdi.i18n.core.plugin;

import java.sql.Statement;
import java.util.*;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;

import io.github.jqdi.i18n.core.exclude.ExcludeI18nChecker;
import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.I18nTableInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;
import io.github.jqdi.i18n.core.provider.I18nDataProvider;
import io.github.jqdi.i18n.core.provider.I18nDataProviderFactory;
import io.github.jqdi.i18n.core.toolkit.CollectionUtils;

/**
 * 国际化字段替换拦截器
 *
 * @author JQ棣
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class I18nFieldValueReplaceInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(I18nFieldValueReplaceInterceptor.class);

    private final I18nReplaceHandler i18nReplaceHandler = new I18nReplaceHandler();

    private final I18nDataProviderFactory i18nDataProviderFactory;

    private final ExcludeI18nChecker excludeI18nChecker;

    public I18nFieldValueReplaceInterceptor(I18nDataProviderFactory i18nDataProviderFactory,
        ExcludeI18nChecker excludeI18nChecker) {
        this.i18nDataProviderFactory = i18nDataProviderFactory;
        this.excludeI18nChecker = excludeI18nChecker;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        if (result == null) {
            return null;
        }

        // 检查是否需要排除国际化
        if (excludeI18nChecker.isExclude(invocation)) {
            return result;
        }

        try {
            if (result instanceof Collection) {
                Collection<?> list = (Collection<?>)result;
                if (CollectionUtils.isEmpty(list)) {
                    return result;
                }
                Class<?> entityClass = list.iterator().next().getClass();
                replaceI18nFieldValue(entityClass, list);
            } else {
                Class<?> entityClass = result.getClass();
                replaceI18nFieldValue(entityClass, Collections.singletonList(result));
            }
        } catch (Exception e) {
            logger.error("replaceI18nFieldValue error", e);
        }
        return result;
    }

    private void replaceI18nFieldValue(Class<?> entityClass, Collection<?> list) {
        I18nTableInfo i18nTableInfo = i18nReplaceHandler.parserI18nMetadata(entityClass);
        if (i18nTableInfo == null) {
            return;
        }
        if (CollectionUtils.isEmpty(i18nTableInfo.getI18nFieldInfoList())) {
            logger.warn("i18nTableInfo.getI18nFieldInfoList() is empty");
            return;
        }

        Set<Object> relatedFieldValueSet =
            i18nReplaceHandler.collectRelatedFieldValue(list, i18nTableInfo.getRelatedValueFromField());
        if (CollectionUtils.isEmpty(relatedFieldValueSet)) {
            return;
        }

        I18nDataProvider i18nDataProvider = i18nDataProviderFactory.newInstance(i18nTableInfo.getI18nDataProvider());
        Map<I18nFieldInfo, List<RelatedI18nValueMapping>> valueMappingListMap =
            i18nDataProvider.getValueMapping(i18nTableInfo, relatedFieldValueSet);
        if (CollectionUtils.isEmpty(valueMappingListMap)) {
            return;
        }

        valueMappingListMap.forEach((i18nFieldInfo, valueMappingList) -> {
            if (CollectionUtils.isEmpty(valueMappingList)) {
                return;
            }
            i18nReplaceHandler.replaceI18nFieldValue(list, i18nTableInfo.getRelatedValueFromField(), i18nFieldInfo.getField(),
                valueMappingList);
        });
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
