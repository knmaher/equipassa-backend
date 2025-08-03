package com.equipassa.equipassa.security.dto;

public record LoginRequest(
        String email,
        String password
) {
}
