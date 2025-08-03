package com.equipassa.equipassa.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization extends SubscribableEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier", length = 50)
    private SubscriptionTier subscriptionTier;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<User> users = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    public Organization() {
    }

    public Organization(final String name, final SubscriptionTier subscriptionTier) {
        this.name = name;
        this.subscriptionTier = subscriptionTier;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSubscriptionTier(final SubscriptionTier subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(final Set<User> users) {
        this.users = users;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public SubscriptionTier getSubscriptionTier() {
        return this.subscriptionTier;
    }
}
