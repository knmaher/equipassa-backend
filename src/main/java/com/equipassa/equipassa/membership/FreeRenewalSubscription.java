package com.equipassa.equipassa.membership;

import com.equipassa.equipassa.model.SubscriptionTier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FreeRenewalSubscription implements RenewalSubscription<Renewable> {

    @Override
    public SubscriptionTier getTier() {
        return SubscriptionTier.FREE;
    }

    @Override
    public LocalDate renew(final LocalDate current) {
        final LocalDate base = current != null && current.isAfter(LocalDate.now())
                ? current : LocalDate.now();
        return base.plusYears(1);
    }
}
