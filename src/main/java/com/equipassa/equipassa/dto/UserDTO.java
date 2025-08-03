package com.equipassa.equipassa.dto;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        String phoneNumber,
        UserRole userRole,
        AddressRequest addressRequest
) {

    public static UserDTO from(final User user) {

        final AddressRequest ar = Optional.ofNullable(user.getAddress())
                .map(address -> new AddressRequest(
                        address.getStreet(),
                        address.getCity(),
                        address.getState(),
                        address.getPostalCode(),
                        address.getCountry()))
                .orElse(null);

        return new UserDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                ar
        );
    }
}