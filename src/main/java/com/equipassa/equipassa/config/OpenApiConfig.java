package com.equipassa.equipassa.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI equipassaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Equipassa API")
                        .description("""
                                Auth uses server-side sessions.
                                - Cookie: EQUIPASSA_SESSION
                                - CSRF header: X-XSRF-TOKEN (CookieCsrfTokenRepository)
                                """)
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.COOKIE)
                                        .name("EQUIPASSA_SESSION"))
                        .addSecuritySchemes("xsrf",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-XSRF-TOKEN")));
    }
}
