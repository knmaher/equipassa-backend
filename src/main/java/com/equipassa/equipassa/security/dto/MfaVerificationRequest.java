package com.equipassa.equipassa.security.dto;

public record MfaVerificationRequest(
        String code,
        boolean rememberDevice
) {
}
