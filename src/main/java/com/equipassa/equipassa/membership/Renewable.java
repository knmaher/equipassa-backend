package com.equipassa.equipassa.membership;

import com.equipassa.equipassa.model.SubscriptionTier;

import java.time.LocalDate;

public interface Renewable {
    SubscriptionTier getSubscriptionTier();

    LocalDate getSubscriptionExpiryDate();

    void setSubscriptionExpiryDate(LocalDate newExpiry);

    Long getId();
}
