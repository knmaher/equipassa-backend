package com.equipassa.equipassa.notification;

import com.equipassa.equipassa.model.Address;
import com.equipassa.equipassa.model.Organization;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.config.EmailProperties;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.notification.template.TemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class EmailChannel implements NotificationChannel {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailProperties emailProperties;

    public EmailChannel(final JavaMailSender mailSender, final TemplateEngine templateEngine, final EmailProperties emailProperties) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.emailProperties = emailProperties;
    }

    @Override
    public void send(final User user, final NotificationPayload payload) {
        final Locale locale = resolveLocale(user);

        final MailTemplate template = payload.template();

        final TemplateEngine.RenderedTemplate rendered;

        try {
            rendered = templateEngine.render(template, locale, payload.subjectArgs(), payload.model());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to render e-mail template", ex);
        }

        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(
                    message, true, StandardCharsets.UTF_8.name());

            helper.setFrom(emailProperties.getFromAddress());
            helper.setTo(user.getEmail());
            helper.setSubject(rendered.subject());
            helper.setText(rendered.html(), true);

            mailSender.send(message);

        } catch (final MessagingException ex) {
            throw new RuntimeException("Failed to send e-mail", ex);
        }

    }

    @Override
    public boolean supports(final User user) {
        return user.isEmailVerified();
    }

    private Locale resolveLocale(final User user) {

        final Address address = user.getAddress();
        if (address != null && address.getCountry() != null && !address.getCountry().isBlank()) {
            final Locale locale = localeFromCountry(address.getCountry());
            if (locale != null) {
                return locale;
            }
        }

        final Organization org = user.getOrganization();
        if (org != null && org.getAddress() != null
                && org.getAddress().getCountry() != null
                && !org.getAddress().getCountry().isBlank()) {

            final Locale locale = localeFromCountry(org.getAddress().getCountry());
            if (locale != null) {
                return locale;
            }
        }

        return Locale.ENGLISH;
    }

    private Locale localeFromCountry(final String country) {

        final String c = country.trim();

        if (c.length() == 2) {
            return Locale.forLanguageTag(c.toLowerCase());
        }

        switch (c.toLowerCase()) {
            case "germany", "deutschland" -> {
                return Locale.GERMAN;
            }
            case "france" -> {
                return Locale.FRENCH;
            }
            case "spain", "españa" -> {
                return new Locale("es");
            }
            default -> {
                return null;
            }
        }
    }
}
