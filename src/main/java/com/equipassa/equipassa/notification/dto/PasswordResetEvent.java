package com.equipassa.equipassa.notification.dto;

public record PasswordResetEvent(Long userId, String token) {
}
