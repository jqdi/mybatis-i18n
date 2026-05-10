package io.github.jqdi.i18n.spring.boot.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import io.github.jqdi.i18n.core.exclude.ExcludeI18nChecker;
import io.github.jqdi.i18n.core.plugin.I18nFieldValueReplaceInterceptor;
import io.github.jqdi.i18n.core.provider.I18nDataProvider;
import io.github.jqdi.i18n.core.provider.I18nDataProviderFactory;
import io.github.jqdi.i18n.spring.boot.starter.exclude.SpringExcludeI18nChecker;
import io.github.jqdi.i18n.spring.boot.starter.exclude.aspect.ExcludeI18nAspect;
import io.github.jqdi.i18n.spring.boot.starter.provider.JdbcTemplateI18nDataProvider;
import io.github.jqdi.i18n.spring.boot.starter.provider.SpringI18nDataProviderFactory;

@Configuration
public class PluginsAutoConfiguration {
    @Bean
    public I18nDataProvider defaultI18nDataProvider(JdbcTemplate jdbcTemplate) {
        return new JdbcTemplateI18nDataProvider(jdbcTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public I18nDataProviderFactory i18nDataProviderFactory(I18nDataProvider defaultI18nDataProvider) {
        return new SpringI18nDataProviderFactory(defaultI18nDataProvider);
    }

    @Bean
    public ExcludeI18nChecker excludeI18nChecker() {
        return new SpringExcludeI18nChecker();
    }

    @Bean
    public I18nFieldValueReplaceInterceptor i18nFieldValueReplaceInterceptor(I18nDataProviderFactory i18nDataProviderFactory,
        ExcludeI18nChecker excludeI18nChecker) {
        return new I18nFieldValueReplaceInterceptor(i18nDataProviderFactory, excludeI18nChecker);
    }

    @Bean
    public ExcludeI18nAspect excludeI18nAspect(SpringExcludeI18nChecker springExcludeI18nChecker) {
        return new ExcludeI18nAspect(springExcludeI18nChecker);
    }
}