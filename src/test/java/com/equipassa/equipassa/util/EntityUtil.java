package com.equipassa.equipassa.util;

import com.equipassa.equipassa.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EntityUtil {

    public static User createUser(
            final String firstName,
            final String lastname,
            final String email,
            final UserRole userRole,
            final boolean emailVerified
    ) {
        final Organization organization = createOrganization();
        return createUser(
                firstName, lastname, email, "Verified@User1", userRole, MembershipStatus.ACTIVE,
                LocalDate.now().plusYears(1), "+491763256987", false,
                null, emailVerified, organization
        );
    }

    public static Organization createOrganization() {
        final Address address = createAddress();
        return createOrganization("Test Org GmbH", SubscriptionTier.FREE, address);
    }

    public static Address createAddress() {
        return createAddress("Street 1", "Street 2", "City 1", "City 2", "Country 1");
    }

    public static User createUser(
            final String firstName,
            final String lastname,
            final String email,
            final String password,
            final UserRole userRole,
            final MembershipStatus membershipStatus,
            final LocalDate membershipExpiration,
            final String phoneNumber,
            final boolean mfaEnabled,
            final String mfaSecret,
            final boolean emailVerified,
            final Organization organization
    ) {
        final User user = new User();
        user.setFirstname(firstName);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(userRole);
        user.setMembershipStatus(membershipStatus);
        user.setSubscriptionExpiryDate(membershipExpiration);
        user.setPhoneNumber(phoneNumber);
        user.setMfaEnabled(mfaEnabled);
        user.setMfaSecret(mfaSecret);
        user.setEmailVerified(emailVerified);
        user.setOrganization(organization);
        return user;
    }

    public static Organization createOrganization(
            final String name,
            final SubscriptionTier subscriptionTier,
            final Address address
    ) {
        final Organization organization = new Organization();
        organization.setName(name);
        organization.setSubscriptionTier(subscriptionTier);
        organization.setAddress(address);
        return organization;
    }

    public static Address createAddress(
            final String street,
            final String city,
            final String state,
            final String postalCode,
            final String country
    ) {
        final Address address = new Address();
        address.setStreet(street);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);
        return address;
    }

    public static ActionToken createActionToken(
            final String token,
            final ActionTokenType actionTokenType,
            final Long userId,
            final LocalDateTime expiresAt,
            final boolean consumed,
            final TokenVisibility visibility
    ) {
        final ActionToken actionToken = new ActionToken();
        actionToken.setToken(token);
        actionToken.setType(actionTokenType);
        actionToken.setUserId(userId);
        actionToken.setExpiresAt(expiresAt);
        actionToken.setConsumed(consumed);
        actionToken.setVisibility(visibility);
        return actionToken;
    }
}
