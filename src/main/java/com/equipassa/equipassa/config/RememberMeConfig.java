package com.equipassa.equipassa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class RememberMeConfig {

    @Bean
    public PersistentTokenRepository persistentTokenRepository(final DataSource dataSource) {
        final JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        // Use migrations to create the table; do NOT enable in prod:
        // repo.setCreateTableOnStartup(true);
        return repo;
    }

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices(
            @Value("${security.remember-me.key}") final String key,
            final UserDetailsService userDetailsService,
            final PersistentTokenRepository tokenRepository,
            final Environment env
    ) {
        final PersistentTokenBasedRememberMeServices svc =
                new PersistentTokenBasedRememberMeServices(key, userDetailsService, tokenRepository);
        svc.setCookieName("EQUIPASSA_REMEMBER_ME");
        svc.setTokenValiditySeconds(60 * 60 * 24 * 14); // 14 days
        svc.setAlwaysRemember(false);
        svc.setUseSecureCookie(env.acceptsProfiles("prod"));
        return svc;
    }
}
