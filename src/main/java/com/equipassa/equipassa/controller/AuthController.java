package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.UserRequest;
import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.security.dto.*;
import com.equipassa.equipassa.security.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextRepository;

    public AuthController(final AuthService authService,
                          final AuthenticationManager authenticationManager, final SecurityContextRepository contextRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.contextRepository = contextRepository;
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
        final String clientIp = getClientIp(httpRequest);

        httpRequest.getSession(true);
        httpRequest.changeSessionId();

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        final var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        contextRepository.saveContext(context, httpRequest, httpResponse);

        authService.auditLoginSuccess(request.email(), clientIp);

        final CustomUserDetails u = (CustomUserDetails) authentication.getPrincipal();
        final AuthResponse body = new AuthResponse(
                null,
                false,
                null,
                u.getId(),
                null,
                u.getRole().name(),
                u.getUsername()
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        final HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof final CustomUserDetails u)) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new UserResponse(u.getId(), u.getUsername(), u.getRole()));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") final String token) {
        final String result = authService.verifyEmail(token);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/password-reset/request")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody @Valid final PasswordResetRequest req) {
        authService.requestPasswordReset(req.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-reset/confirm")
    public ResponseEntity<Void> confirmPasswordReset(@RequestBody @Valid final PasswordResetConfirmRequest req) {
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
