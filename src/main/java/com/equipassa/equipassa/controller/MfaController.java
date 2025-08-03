package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.CurrentUser;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.security.dto.AuthResponse;
import com.equipassa.equipassa.security.dto.MfaEnableRequest;
import com.equipassa.equipassa.security.dto.MfaQrResponse;
import com.equipassa.equipassa.security.dto.MfaVerificationRequest;
import com.equipassa.equipassa.security.service.AuthService;
import com.equipassa.equipassa.security.service.MfaService;
import com.equipassa.equipassa.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mfa")
public class MfaController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MfaService mfaService;
    private final AuthService authService;

    public MfaController(
            final UserRepository userRepository,
            final UserService userService,
            final MfaService mfaService,
            final AuthService authService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mfaService = mfaService;
        this.authService = authService;
    }

    @PostMapping("/enable")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> enableMfa(
            @RequestBody @Valid final MfaEnableRequest request,
            @CurrentUser final CustomUserDetails currentUser
    ) {
        final User user = userService.getById(currentUser.getId());

        if (!mfaService.validateCode(user.getMfaSecret(), request.code())) {
            throw new BadCredentialsException("Invalid verification code");
        }

        user.setMfaEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/qr-code")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MfaQrResponse> getMfaQrCode(
            @CurrentUser final CustomUserDetails currentUser
    ) throws QrGenerationException {
        final User user = userService.getById(currentUser.getId());
        final String qrCodeUri = mfaService.generateQrCodeUri(user.getMfaSecret(), user.getEmail());
        return ResponseEntity.ok(new MfaQrResponse(qrCodeUri));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verifyMfa(
            @RequestHeader("Authorization") final String authHeader,
            @RequestBody @Valid final MfaVerificationRequest request
    ) {
        final String tempToken = authHeader.substring(7);
        final AuthResponse response = authService.verifyMfa(tempToken, request.code(), request.rememberDevice());
        return ResponseEntity.ok(response);
    }
}
