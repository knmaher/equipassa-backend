package com.equipassa.equipassa.model;

import com.equipassa.equipassa.validation.ValidPassword;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
public class User extends SubscribableEntity {

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstname;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastname;

    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "password", nullable = false, length = 97)
    @ValidPassword
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "membership_status", nullable = false)
    private MembershipStatus membershipStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier", length = 50)
    private SubscriptionTier subscriptionTier;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "mfa_enabled")
    private boolean mfaEnabled;

    @Column(name = "mfa_secret", length = 32)
    private String mfaSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(final String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(final UserRole role) {
        this.role = role;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(final MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(final boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(final String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(final Organization organization) {
        this.organization = organization;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public SubscriptionTier getSubscriptionTier() {
        return subscriptionTier;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final User user)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", membershipStatus='" + membershipStatus + '\'' +
                ", membershipStatus=" + membershipStatus +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                ", version=" + getVersion() +
                '}';
    }
}
