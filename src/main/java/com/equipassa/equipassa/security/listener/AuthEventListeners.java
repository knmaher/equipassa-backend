package com.equipassa.equipassa.security.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthEventListeners {
    private static final Logger log = LoggerFactory.getLogger(AuthEventListeners.class);

    @EventListener
    public void onSuccess(final AuthenticationSuccessEvent e) {
        final String name = e.getAuthentication().getName();
        log.info("AUTH SUCCESS user={}", name);
    }

    @EventListener
    public void onFailure(final AbstractAuthenticationFailureEvent e) {
        final String name = e.getAuthentication().getName();
        final String reason = e.getException().getMessage();
        log.warn("AUTH FAILURE user={} reason={}", name, reason);
    }
}
