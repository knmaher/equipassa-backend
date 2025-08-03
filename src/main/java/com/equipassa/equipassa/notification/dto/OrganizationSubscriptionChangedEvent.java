package com.equipassa.equipassa.notification.dto;

import java.time.LocalDate;

public record OrganizationSubscriptionChangedEvent(Long userId, LocalDate newExpiry) {
}
