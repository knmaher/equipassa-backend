package com.equipassa.equipassa.security.ajax;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class AjaxUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper om = new ObjectMapper();

    public AjaxUsernamePasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/auth/ajax-login", "POST"));
        setAuthenticationSuccessHandler(new AjaxAuthenticationSuccessHandler());
        setAuthenticationFailureHandler(new AjaxAuthenticationFailureHandler());
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, AuthenticationException {
        final JsonNode body = om.readTree(request.getInputStream());
        final String email = body.path("email").asText(null);
        final String password = body.path("password").asText(null);
        final UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(auth);
    }
}
