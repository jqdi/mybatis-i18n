package io.github.jqdi.i18n.spring.boot.starter.provider;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;

import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.I18nTableInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;
import io.github.jqdi.i18n.core.provider.I18nDataProvider;

/**
 * 基于JdbcTemplate的国际化数据提供者
 *
 * @author JQ棣
 */
public class JdbcTemplateI18nDataProvider implements I18nDataProvider {
    /**
     * <pre>
     * 构建查询sql：
     * select {i18nRelatedColumn},[i18nColumns]
     * from {i18nTable}
     * where {i18nRelatedColumn} in (?,?,...)
     * and {i18nLocaleColumn} = ?
     * </pre>
     */
    private static final String SQL_PATTERN =
        "select {i18nRelatedColumn},[i18nColumns] from {i18nTable} where {i18nRelatedColumn} in ({relatedFieldPlaceholders}) and {i18nLocaleColumn} = ?";

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateI18nDataProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<I18nFieldInfo, List<RelatedI18nValueMapping>> getValueMapping(I18nTableInfo i18nTableInfo,
        Set<Object> relatedFieldValueSet) {
        String i18nTable = i18nTableInfo.getI18nTable();
        String i18nRelatedColumn = i18nTableInfo.getI18nRelatedColumn();
        String i18nLocaleColumn = i18nTableInfo.getI18nLocaleColumn();

        // 字段列拼接，多个逗号分隔
        List<I18nFieldInfo> i18nFieldInfoList = i18nTableInfo.getI18nFieldInfoList();
        String i18nColumns = i18nFieldInfoList.stream().map(I18nFieldInfo::getI18nColumn).collect(Collectors.joining(","));

        String relatedFieldPlaceholders = String.join(",", Collections.nCopies(relatedFieldValueSet.size(), "?"));

        String i18nQuerySql = SQL_PATTERN
                .replace("{i18nRelatedColumn}", i18nRelatedColumn)
                .replace("[i18nColumns]", i18nColumns)
                .replace("{i18nTable}", i18nTable)
                .replace("{relatedFieldPlaceholders}", relatedFieldPlaceholders)
                .replace("{i18nLocaleColumn}", i18nLocaleColumn);

        List<Object> argList = new ArrayList<>(relatedFieldValueSet);
        argList.add(LocaleContextHolder.getLocale().toLanguageTag());
        List<Map<String, Object>> i18nEntityList = jdbcTemplate.queryForList(i18nQuerySql, argList.toArray());

        return i18nFieldInfoList.stream()
            .collect(Collectors.toMap(f -> f,
                i18nFieldInfo -> i18nEntityList.stream()
                    .map(v -> new RelatedI18nValueMapping(v.get(i18nRelatedColumn), v.get(i18nFieldInfo.getI18nColumn())))
                    .collect(Collectors.toList())));
    }
}
