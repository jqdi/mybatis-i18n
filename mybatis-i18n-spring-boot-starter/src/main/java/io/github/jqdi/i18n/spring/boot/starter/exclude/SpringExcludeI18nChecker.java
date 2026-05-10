package io.github.jqdi.i18n.spring.boot.starter.exclude;

import org.apache.ibatis.plugin.Invocation;

import io.github.jqdi.i18n.core.exclude.ExcludeI18nChecker;

/**
 * Spring环境下的国际化排除检查器
 *
 * @author JQ棣
 */
public class SpringExcludeI18nChecker implements ExcludeI18nChecker {
    /**
     * 支持嵌套层级
     */
    private static final ThreadLocal<Integer> EXCLUDE_LEVEL = new ThreadLocal<>();

    @Override
    public boolean isExclude(Invocation invocation) {
        Integer level = EXCLUDE_LEVEL.get();
        return level != null && level > 0;
    }

    public void increment() {
        Integer level = EXCLUDE_LEVEL.get();
        if (level == null) {
            level = 0;
        }
        EXCLUDE_LEVEL.set(level + 1);
    }

    public void decrement() {
        Integer level = EXCLUDE_LEVEL.get();
        if (level == null) {
            return;
        }

        level--;
        if (level <= 0) {
            EXCLUDE_LEVEL.remove();
        } else {
            EXCLUDE_LEVEL.set(level);
        }
    }
}