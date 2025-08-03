package com.equipassa.equipassa.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserProfileRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @Email @NotBlank String email,
        String phoneNumber,
        @Valid AddressRequest addressRequest,
        Boolean mfaEnabled
) {
}
