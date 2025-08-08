package com.equipassa.equipassa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.format.support.DefaultFormattingConversionService;
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
        s.setUseSecureCookie(env.acceptsProfiles("prod")); // Secure only on HTTPS
        s.setSameSite("None"); // cross-site (frontend on another origin)
        return s;
    }

    @Bean
    public DefaultFormattingConversionService conversionService() {
        final DefaultFormattingConversionService cs = new DefaultFormattingConversionService();
        cs.addConverter(String.class, Instant.class, Instant::parse);
        cs.addConverter(Instant.class, String.class, Instant::toString);
        cs.addConverter(String.class, UUID.class, UUID::fromString);
        cs.addConverter(UUID.class, String.class, UUID::toString);
        return cs;
    }
}
