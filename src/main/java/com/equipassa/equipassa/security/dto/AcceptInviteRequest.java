package com.equipassa.equipassa.security.dto;

import com.equipassa.equipassa.dto.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AcceptInviteRequest(
        @NotBlank String token,
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String phoneNumber,
        @NotNull AddressRequest address
) {}
