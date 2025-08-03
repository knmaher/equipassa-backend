package com.equipassa.equipassa.service;

import com.equipassa.equipassa.dto.AddressRequest;
import com.equipassa.equipassa.dto.UserProfileRequest;
import com.equipassa.equipassa.model.Address;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(final Long id) {
        return userRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

    @Transactional
    public User updateProfile(final Long userId, final UserProfileRequest userProfileRequest) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstname(userProfileRequest.firstname());
        user.setLastname(userProfileRequest.lastname());
        user.setEmail(userProfileRequest.email());
        user.setPhoneNumber(userProfileRequest.phoneNumber());

        if (userProfileRequest.addressRequest() != null) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
                user.setAddress(address);
            }

            final AddressRequest ar = userProfileRequest.addressRequest();
            address.setStreet(ar.street());
            address.setCity(ar.city());
            address.setState(ar.state());
            address.setPostalCode(ar.postalCode());
            address.setCountry(ar.country());
        }

        return userRepository.save(user);
    }
}
