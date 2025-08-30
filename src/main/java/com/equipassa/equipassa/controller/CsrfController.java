package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.CsrfDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class CsrfController {

    @GetMapping("/csrf")
    @Operation(summary = "Get CSRF token", security = {}) // ← no auth for this op
    public CsrfDto csrf(@Parameter(hidden = true) final CsrfToken token) {
        return new CsrfDto(token.getHeaderName(), token.getParameterName(), token.getToken());
    }
}