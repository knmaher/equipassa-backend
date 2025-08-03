package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new CustomUserDetails(
                        user.getId(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole(),
                        user.isMfaEnabled(),
                        user.getOrganization() != null ? user.getOrganization().getId() : null
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
