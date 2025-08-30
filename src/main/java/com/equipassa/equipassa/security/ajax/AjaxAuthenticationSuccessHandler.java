package com.equipassa.equipassa.security.ajax;

import com.equipassa.equipassa.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper om = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        final CustomUserDetails u = (CustomUserDetails) authentication.getPrincipal();
        om.writeValue(response.getOutputStream(), Map.of(
                "userId", u.getId(),
                "email", u.getUsername(),
                "userRole", u.getRole().name()
        ));
    }
}
