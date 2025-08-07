package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.UserRequest;
import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.security.dto.*;
import com.equipassa.equipassa.security.service.AuthService;
import com.equipassa.equipassa.util.CookieUtils;
import com.equipassa.equipassa.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(final AuthService authService, final JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register-org")
    public ResponseEntity<OrgUserResponse> registerOrg(@RequestBody @Valid final OrgRegistrationRequest request) {
        final OrgUserResponse response = authService.createOrganizationAndAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody @Valid final UserRequest request, final HttpServletRequest httpRequest
    ) {
        final String clientIp = getClientIp(httpRequest);
        return ResponseEntity.ok(authService.register(request, clientIp));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid final LoginRequest request,
            final HttpServletRequest httpRequest,
            final HttpServletResponse httpResponse
    ) {
        final AuthResponse tokens = authService.login(request, getClientIp(httpRequest));
        CookieUtils.addHttpOnlyCookie(
                httpResponse, "accessToken",
                tokens.token(),
                Duration.ofMillis(jwtUtil.getExpirationTime()),
                "/", true, "None"
        );

        CookieUtils.addHttpOnlyCookie(
                httpResponse, "refreshToken",
                tokens.refreshToken(),
                Duration.ofDays(7),
                "/api/auth/refresh", true, "None"
        );

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public void logout(
            @CookieValue(value = "accessToken", required = false) final String cookieToken,
            @RequestHeader(value = "Authorization", required = false) final String headerToken,
            final HttpServletResponse response
    ) {
        String token = cookieToken;
        if (token == null && headerToken != null && headerToken.startsWith("Bearer ")) {
            token = headerToken.substring(7);
        }

        if (token != null) {
            authService.logout(token);
        }

        CookieUtils.clearCookie(response, "accessToken", "/");
        CookieUtils.clearCookie(response, "refreshToken", "/");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") final String token) {
        final String result = authService.verifyEmail(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public void refreshToken(
            @CookieValue("refreshToken") final String refreshTokenCookie,
            final HttpServletResponse response
    ) {
        final AuthResponse tokens = authService.refreshToken(refreshTokenCookie);
        CookieUtils.addHttpOnlyCookie(
                response, "accessToken", tokens.token(),
                Duration.ofMillis(jwtUtil.getExpirationTime()),
                "/", true, "Lax"
        );
        CookieUtils.addHttpOnlyCookie(
                response, "refreshToken", tokens.refreshToken(),
                Duration.ofDays(7),
                "/api/auth/refresh", true, "Strict"
        );
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(
            @RequestBody @Valid final PasswordResetRequest req
    ) {
        authService.requestPasswordReset(req.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(
            @RequestBody @Valid final PasswordResetConfirmRequest req
    ) {
        authService.confirmPasswordReset(req.token(), req.newPassword());
        return ResponseEntity.noContent().build();
    }

    private String getClientIp(final HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
