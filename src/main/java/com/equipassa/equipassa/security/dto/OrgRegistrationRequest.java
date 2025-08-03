package com.equipassa.equipassa.security.dto;

import com.equipassa.equipassa.dto.AddressRequest;
import com.equipassa.equipassa.model.SubscriptionTier;
import com.equipassa.equipassa.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrgRegistrationRequest(
        @NotBlank String organizationName,

        @NotBlank String adminFirstname,

        @NotBlank String adminLastname,

        @NotBlank @Email String adminEmail,

        @ValidPassword
        String adminPassword,

        SubscriptionTier subscriptionTier,

        @NotNull AddressRequest addressRequest
) {
}
