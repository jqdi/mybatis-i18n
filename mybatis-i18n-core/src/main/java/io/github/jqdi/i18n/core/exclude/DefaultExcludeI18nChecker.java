package io.github.jqdi.i18n.core.exclude;

import java.lang.reflect.Method;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import io.github.jqdi.i18n.core.annotation.ExcludeI18n;

/**
 * 默认排除检查器
 *
 * @author JQ棣
 */
public class DefaultExcludeI18nChecker implements ExcludeI18nChecker {

    @Override
    public boolean isExclude(Invocation invocation) {
        Object target = invocation.getTarget();
        if (!(target instanceof StatementHandler)) {
            return false;
        }
        StatementHandler statementHandler = (StatementHandler)target;
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        MappedStatement ms = (MappedStatement)metaObject.getValue("delegate.mappedStatement");

        String id = ms.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);

        Class<?> mapperClass;
        try {
            mapperClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Method targetMethod = null;
        for (Method m : mapperClass.getDeclaredMethods()) {
            if (m.getName().equals(methodName)) {
                targetMethod = m;
                break;
            }
        }
        if (targetMethod == null) {
            return false;
        }
        ExcludeI18n annotation = targetMethod.getAnnotation(ExcludeI18n.class);
        return annotation != null;
    }
}