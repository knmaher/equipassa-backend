package com.equipassa.equipassa.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.time.Instant;
import java.util.UUID;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 min
public class SessionConfig {

    private final Environment env;

    public SessionConfig(final Environment env) {
        this.env = env;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        final DefaultCookieSerializer s = new DefaultCookieSerializer();
        s.setCookieName("EQUIPASSA_SESSION");
        s.setCookiePath("/");
        s.setUseHttpOnlyCookie(true);

        final boolean prod = env.acceptsProfiles(Profiles.of("prod"));

        if (prod) {
            s.setSameSite("None");
            s.setUseSecureCookie(true);
        } else {
            s.setSameSite("Lax");
            s.setUseSecureCookie(false);
        }

        return s;
    }

    @Bean
    public FormattingConversionService conversionService() {
        final ApplicationConversionService applicationConversionService = new ApplicationConversionService();

        applicationConversionService.addConverter(String.class, Instant.class, Instant::parse);
        applicationConversionService.addConverter(Instant.class, String.class, Instant::toString);
        applicationConversionService.addConverter(String.class, UUID.class, UUID::fromString);
        applicationConversionService.addConverter(UUID.class, String.class, UUID::toString);

        final SerializingConverter serializingConverter = new SerializingConverter();
        final DeserializingConverter deserializingConverter = new DeserializingConverter();

        applicationConversionService.addConverter(serializingConverter);
        applicationConversionService.addConverter(deserializingConverter);

        return applicationConversionService;
    }

    @Bean
    public SessionRepositoryCustomizer<JdbcIndexedSessionRepository> jdbcCustomizer(
            final FormattingConversionService conversionService) {
        return (repo) -> repo.setConversionService(conversionService);
    }
}
