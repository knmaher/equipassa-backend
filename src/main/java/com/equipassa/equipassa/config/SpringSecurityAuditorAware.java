package com.equipassa.equipassa.config;

import com.equipassa.equipassa.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private static final String anonymousUser = "anonymousUser";

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(anonymousUser);
        }

        final Object principal = authentication.getPrincipal();
        if (principal instanceof final CustomUserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }

        return Optional.empty();
    }
}
