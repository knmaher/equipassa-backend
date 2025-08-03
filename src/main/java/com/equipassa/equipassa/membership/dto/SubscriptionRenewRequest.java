package com.equipassa.equipassa.membership.dto;

import jakarta.validation.constraints.Min;

public record SubscriptionRenewRequest(
        @Min(value = 1, message = "Must extend by at least 1 month")
        int extensionMonths
) {
}
