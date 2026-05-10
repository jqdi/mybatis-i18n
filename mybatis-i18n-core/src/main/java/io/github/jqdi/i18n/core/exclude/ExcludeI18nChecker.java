package io.github.jqdi.i18n.core.exclude;

import org.apache.ibatis.plugin.Invocation;

/**
 * 国际化排除检查器
 *
 * @author JQ棣
 */
public interface ExcludeI18nChecker {

    /**
     * 检查是否需要排除国际化处理
     *
     * @param invocation MyBatis拦截器调用对象
     * @return true-需要排除, false-不需要排除
     */
    boolean isExclude(Invocation invocation);
}