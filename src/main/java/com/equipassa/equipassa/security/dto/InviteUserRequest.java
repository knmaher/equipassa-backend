package com.equipassa.equipassa.security.dto;

import com.equipassa.equipassa.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InviteUserRequest(
        @NotBlank @Email String email,
        @NotNull UserRole role
) {}
