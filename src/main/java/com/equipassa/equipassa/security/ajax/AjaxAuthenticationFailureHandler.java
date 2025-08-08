package com.equipassa.equipassa.security.ajax;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.Map;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        om.writeValue(response.getOutputStream(), Map.of(
                "error", "Bad credentials",
                "message", exception.getMessage()
        ));
    }
}
