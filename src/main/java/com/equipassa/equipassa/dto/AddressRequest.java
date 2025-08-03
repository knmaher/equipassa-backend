package com.equipassa.equipassa.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank String street,
        @NotBlank String city,
        @NotBlank String state,
        @NotBlank String postalCode,
        @NotBlank String country
) {
}
