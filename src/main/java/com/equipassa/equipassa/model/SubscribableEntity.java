package com.equipassa.equipassa.model;

import com.equipassa.equipassa.membership.Renewable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDate;

@MappedSuperclass
public abstract class SubscribableEntity extends Auditable implements Renewable {
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Override
    public LocalDate getSubscriptionExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setSubscriptionExpiryDate(final LocalDate newExpiry) {
        this.expiryDate = newExpiry;
    }

    @Override
    public Long getId() {
        return super.getId();
    }
}
