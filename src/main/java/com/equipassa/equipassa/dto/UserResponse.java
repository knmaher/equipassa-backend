package com.equipassa.equipassa.dto;

import com.equipassa.equipassa.model.UserRole;

public record UserResponse(
        Long id,
        String email,
        UserRole userRole
) {
}
