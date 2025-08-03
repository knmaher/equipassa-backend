package com.equipassa.equipassa.security.dto;

import com.equipassa.equipassa.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record PasswordResetConfirmRequest(
        @NotBlank String token,
        @ValidPassword String newPassword
) {
}
