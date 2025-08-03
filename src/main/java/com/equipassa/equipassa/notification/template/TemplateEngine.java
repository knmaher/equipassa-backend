package com.equipassa.equipassa.notification.template;

import freemarker.template.Configuration;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Locale;
import java.util.Map;

@Component
public class TemplateEngine {

    private final Configuration freemarkerConfiguration;
    private final MessageSource messageSource;


    public TemplateEngine(final Configuration freemarkerConfiguration, final MessageSource messageSource) {
        this.freemarkerConfiguration = freemarkerConfiguration;
        this.messageSource = messageSource;
    }

    public RenderedTemplate render(
            final MailTemplate mailTemplate,
            final Locale locale,
            final Object[] subjectArgs,
            final Map<String, Object> model
    ) throws Exception {

        final String subject = messageSource.getMessage(mailTemplate.subjectKey(), subjectArgs, locale);
        final String file = mailTemplate.fileName();

        final String html = FreeMarkerTemplateUtils
                .processTemplateIntoString(freemarkerConfiguration.getTemplate(file), model);

        return new RenderedTemplate(subject, html);
    }

    public record RenderedTemplate(String subject, String html) {
    }
}
