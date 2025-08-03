package com.equipassa.equipassa.dto;

import com.equipassa.equipassa.model.MembershipStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        MembershipStatus membershipStatus,
        @NotBlank String phoneNumber,
        @NotNull AddressRequest address
) {
}
