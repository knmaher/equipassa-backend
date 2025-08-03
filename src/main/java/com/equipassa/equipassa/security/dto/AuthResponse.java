package com.equipassa.equipassa.security.dto;

public record AuthResponse(
        String token,
        boolean mfaRequired,
        Long expiresIn,
        Long userId,
        String refreshToken,
        String userRole,
        String email
) {
}
