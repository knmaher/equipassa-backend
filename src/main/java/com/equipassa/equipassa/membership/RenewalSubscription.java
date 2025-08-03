package com.equipassa.equipassa.membership;

import com.equipassa.equipassa.model.SubscriptionTier;

import java.time.LocalDate;

public interface RenewalSubscription<T extends Renewable> {
    SubscriptionTier getTier();

    LocalDate renew(LocalDate currentExpiry);
}
