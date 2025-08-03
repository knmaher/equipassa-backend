package com.equipassa.equipassa.security.dto;

public record OrgUserResponse(
        Long organizationId,
        String organizationName,
        Long adminUserId,
        String adminEmail
) {
}
