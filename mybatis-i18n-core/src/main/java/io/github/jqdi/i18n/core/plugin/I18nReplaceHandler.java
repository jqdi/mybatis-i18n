package io.github.jqdi.i18n.core.plugin;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import io.github.jqdi.i18n.core.annotation.I18nField;
import io.github.jqdi.i18n.core.annotation.I18nTable;
import io.github.jqdi.i18n.core.metadata.I18nFieldInfo;
import io.github.jqdi.i18n.core.metadata.I18nTableInfo;
import io.github.jqdi.i18n.core.metadata.RelatedI18nValueMapping;
import io.github.jqdi.i18n.core.toolkit.CollectionUtils;
import io.github.jqdi.i18n.core.toolkit.ReflectionKit;

/**
 * 国际化字段值替换处理器
 *
 * @author JQ棣
 */
public class I18nReplaceHandler {
    private static final Log logger = LogFactory.getLog(I18nReplaceHandler.class);

    private final ConcurrentHashMap<Class<?>, I18nTableInfo> CLASS_METADATA_CACHE = new ConcurrentHashMap<>();

    /**
     * 解析类中使用的@I18nTable 和 @I18nField注解的字段
     *
     * @param entityClass 数据库实体类class
     * @return 国际化表信息
     */
    public I18nTableInfo parserI18nMetadata(Class<?> entityClass) {
        I18nTable i18nTable = entityClass.getAnnotation(I18nTable.class);
        if (i18nTable == null) {
            return null;
        }
        // 参考ReflectionKit.getFieldList实现缓存
        return CollectionUtils.computeIfAbsent(CLASS_METADATA_CACHE, entityClass, k -> {
            List<I18nFieldInfo> i18nFieldInfoList = new ArrayList<>();
            List<Field> allFields = ReflectionKit.getFieldList(k);
            Map<String, Field> fieldMap = ReflectionKit.getFieldMap(k);
            for (Field field : allFields) {
                I18nField i18nField = field.getAnnotation(I18nField.class);
                if (i18nField == null) {
                    continue;
                }
                String i18nColumn = i18nField.i18nColumn();
                i18nFieldInfoList.add(new I18nFieldInfo(field, i18nColumn));
            }
            return new I18nTableInfo(i18nFieldInfoList, i18nTable.i18nTable(), i18nTable.i18nRelatedColumn(),
                fieldMap.get(i18nTable.relatedValueFromField()), i18nTable.i18nLocaleColumn(), i18nTable.i18nDataProvider());
        });
    }

    /**
     * 收集关联字段值（去重）
     *
     * @param entityList    实体类结果集
     * @param relatedValueFromField 关联字段值字段
     * @return 关联字段值列表（去重）
     */
    public Set<Object> collectRelatedFieldValue(Collection<?> entityList, Field relatedValueFromField) {
        // 使用反射将entityList中的每个对象的relatedValueFromField对应的值取出
        Set<Object> relatedFieldValueSet = new HashSet<>();
        for (Object entity : entityList) {
            Object relatedValue = getFieldValue(entity, relatedValueFromField);
            if (relatedValue == null) {
                logger.warn(relatedValueFromField.getName() + " relatedValue is null");
                continue;
            }
            relatedFieldValueSet.add(relatedValue);
        }
        return relatedFieldValueSet;
    }

    /**
     * 替换国际化字段值
     *
     * @param entityList    实体类结果集
     * @param field 国际化字段
     */
    public void replaceI18nFieldValue(Collection<?> entityList, Field relatedValueFromField, Field field,
        List<RelatedI18nValueMapping> valueMappingList) {
        // 注入国际化数据
        Map<Object, Object> i18nRelatedColumnI18nValueMap =
            valueMappingList.stream().filter(v -> v.getI18nRelatedValue() != null && v.getI18nValue() != null).collect(Collectors
                .toMap(RelatedI18nValueMapping::getI18nRelatedValue, RelatedI18nValueMapping::getI18nValue, (a, b) -> b));
        for (Object entity : entityList) {
            Object relatedValue = getFieldValue(entity, relatedValueFromField);
            if (relatedValue == null) {
                logger.warn(relatedValueFromField.getName() + " relatedValue is null");
                continue;
            }

            Object i18nValue = i18nRelatedColumnI18nValueMap.get(relatedValue);
            if (i18nValue == null) {
                logger.warn(relatedValue + " i18nValue is null");
                continue;
            }
            setFieldValue(entity, field, i18nValue);
        }
    }

    private static Object getFieldValue(Object entity, Field field) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);
            field.setAccessible(false);
            return value;
        } catch (ReflectiveOperationException e) {
            logger.error("Error: Cannot read field in " + entity.getClass().getSimpleName() + ".  Cause:", e);
        }
        return null;
    }

    private static void setFieldValue(Object entity, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
            field.setAccessible(false);
        } catch (ReflectiveOperationException e) {
            logger.error("Error: Cannot set field in " + entity.getClass().getSimpleName() + ".  Cause:", e);
        }
    }

}
