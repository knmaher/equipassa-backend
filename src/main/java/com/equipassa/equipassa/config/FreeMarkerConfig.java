package com.equipassa.equipassa.config;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FreeMarkerConfig {

    private static final String[] LOCALE_FOLDERS = {
            "/templates/emails/de",
            "/templates/emails/en"
    };

    @Bean
    @Primary
    public freemarker.template.Configuration freemarkerConfiguration() {

        freemarker.template.Configuration freemarkerConfig = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        freemarkerConfig.setDefaultEncoding("UTF-8");
        freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerConfig.setLogTemplateExceptions(false);
        freemarkerConfig.setWrapUncheckedExceptions(true);

        TemplateLoader[] loaders = new TemplateLoader[LOCALE_FOLDERS.length];
        for (int i = 0; i < LOCALE_FOLDERS.length; i++) {
            loaders[i] = new ClassTemplateLoader(getClass(), LOCALE_FOLDERS[i]);
        }

        freemarkerConfig.setTemplateLoader(new MultiTemplateLoader(loaders));

        return freemarkerConfig;
    }
}
