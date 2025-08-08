package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.CustomUserDetails;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();

    public CustomUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User u = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        final CustomUserDetails cud = new CustomUserDetails(
                u.getId(), u.getEmail(), u.getPassword(), u.getRole(),
                u.isMfaEnabled(), u.getOrganization() != null ? u.getOrganization().getId() : null
        );
        checker.check(cud); // enabled, non-locked, non-expired
        return cud;
    }
}
