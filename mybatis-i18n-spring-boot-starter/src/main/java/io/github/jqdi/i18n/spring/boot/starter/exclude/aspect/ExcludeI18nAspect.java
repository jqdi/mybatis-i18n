package io.github.jqdi.i18n.spring.boot.starter.exclude.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import io.github.jqdi.i18n.core.annotation.ExcludeI18n;
import io.github.jqdi.i18n.spring.boot.starter.exclude.SpringExcludeI18nChecker;

/**
 * 排除国际化切面
 *
 * @author JQ棣
 */
@Aspect
public class ExcludeI18nAspect {
    private final SpringExcludeI18nChecker springExcludeI18nChecker;

    public ExcludeI18nAspect(SpringExcludeI18nChecker springExcludeI18nChecker) {
        this.springExcludeI18nChecker = springExcludeI18nChecker;
    }

    @Around("@annotation(excludeI18n)")
    public Object around(ProceedingJoinPoint joinPoint, ExcludeI18n excludeI18n) throws Throwable {
        try {
            // 设置排除国际化
            springExcludeI18nChecker.increment();
            return joinPoint.proceed();
        } finally {
            springExcludeI18nChecker.decrement();
        }
    }
}