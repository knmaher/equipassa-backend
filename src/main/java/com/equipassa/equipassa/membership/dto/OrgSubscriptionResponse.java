package com.equipassa.equipassa.membership.dto;

import com.equipassa.equipassa.model.SubscriptionTier;

import java.time.LocalDate;

public record OrgSubscriptionResponse(
        Long organizationId,
        SubscriptionTier tier,
        LocalDate expiryDate
) {
}
