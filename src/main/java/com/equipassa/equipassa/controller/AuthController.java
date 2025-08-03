package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.UserRequest;
import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.security.dto.*;
import com.equipassa.equipassa.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
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
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid final LoginRequest request, final HttpServletRequest httpRequest) {
        final String clientIp = getClientIp(httpRequest);
        return ResponseEntity.ok(authService.login(request, clientIp));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") final String authHeader) {
        final String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") final String token) {
        final String result = authService.verifyEmail(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody @Valid final RefreshTokenRequest request
    ) {
        final AuthResponse response = authService.refreshToken(request.refreshToken());
        return ResponseEntity.ok(response);
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
