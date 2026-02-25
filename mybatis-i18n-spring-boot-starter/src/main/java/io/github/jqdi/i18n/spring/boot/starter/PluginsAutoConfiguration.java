package io.github.jqdi.i18n.spring.boot.starter;

import io.github.jqdi.i18n.core.plugin.I18nFieldValueReplaceInterceptor;
import io.github.jqdi.i18n.core.provider.I18nDataProvider;
import io.github.jqdi.i18n.core.provider.I18nDataProviderFactory;
import io.github.jqdi.i18n.spring.boot.starter.provider.JdbcTemplateI18nDataProvider;
import io.github.jqdi.i18n.spring.boot.starter.provider.SpringI18nDataProviderFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
    public I18nFieldValueReplaceInterceptor i18nFieldValueReplaceInterceptor(I18nDataProviderFactory i18nDataProviderFactory) {
        return new I18nFieldValueReplaceInterceptor(i18nDataProviderFactory);
    }
}
