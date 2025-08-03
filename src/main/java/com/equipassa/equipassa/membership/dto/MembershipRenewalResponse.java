package com.equipassa.equipassa.membership.dto;

import java.time.LocalDate;

public record MembershipRenewalResponse(
        Long userId,
        LocalDate newExpirationDate
) {
}
