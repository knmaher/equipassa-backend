package com.equipassa.equipassa.membership;

import com.equipassa.equipassa.model.SubscriptionTier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Service
public class RenewalContext {

    private final Map<SubscriptionTier, RenewalSubscription<? extends Renewable>> strategies;

    public RenewalContext(final List<RenewalSubscription<? extends Renewable>> all) {
        this.strategies = all.stream()
                .collect(toUnmodifiableMap(RenewalSubscription::getTier, s -> s));
    }

    @SuppressWarnings("unchecked")
    public <T extends Renewable> LocalDate renew(final T subject) {
        final RenewalSubscription<T> renewalSubscription =
                (RenewalSubscription<T>) strategies.get(subject.getSubscriptionTier());
        if (renewalSubscription == null)
            throw new IllegalStateException("No strategy for " + subject.getSubscriptionTier());
        return renewalSubscription.renew(subject.getSubscriptionExpiryDate());
    }
}
