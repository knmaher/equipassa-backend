package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.dto.AddressRequest;
import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.model.*;
import com.equipassa.equipassa.repository.ActionTokenRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.dto.AcceptInviteRequest;
import com.equipassa.equipassa.security.dto.InviteUserRequest;
import com.equipassa.equipassa.notification.dto.InvitationEvent;
import org.springframework.context.ApplicationEventPublisher;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationService {

    @Value("${invite.expiration-days:3}")
    private int inviteExpirationDays;

    private final UserRepository userRepository;
    private final ActionTokenRepository actionTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher events;

    public InvitationService(
            final UserRepository userRepository,
            final ActionTokenRepository actionTokenRepository,
            final PasswordEncoder passwordEncoder,
            final ApplicationEventPublisher events
    ) {
        this.userRepository = userRepository;
        this.actionTokenRepository = actionTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.events = events;
    }

    @Transactional
    public ActionToken sendInvitation(final InviteUserRequest req, final Long orgAdminId) {
        final User admin = userRepository.findById(orgAdminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        final Organization org = Optional.ofNullable(admin.getOrganization())
                .orElseThrow(() -> new IllegalStateException("Admin not linked to organization"));

        if (req.role() != UserRole.MEMBER && req.role() != UserRole.STAFF) {
            throw new IllegalArgumentException("Role must be MEMBER or STAFF");
        }

        final ActionToken token = new ActionToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(admin.getId());
        token.setType(ActionTokenType.INVITATION);
        token.setExpiresAt(LocalDateTime.now().plusDays(inviteExpirationDays));
        token.setConsumed(false);
        token.setVisibility(TokenVisibility.PRIVATE);
        token.setInviteEmail(req.email());
        token.setInviteRole(req.role());

        final ActionToken saved = actionTokenRepository.save(token);
        events.publishEvent(new InvitationEvent(saved));

        return saved;
    }

    @Transactional
    public UserResponse acceptInvitation(final AcceptInviteRequest req) {
        final ActionToken token = actionTokenRepository
                .findByTokenAndTypeAndVisibilityAndConsumedFalse(
                        req.token(), ActionTokenType.INVITATION, TokenVisibility.PRIVATE)
                .orElse(null);
        if (token == null || LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new IllegalArgumentException("Invalid or expired invitation");
        }

        if (!token.getInviteEmail().equalsIgnoreCase(req.email())) {
            throw new IllegalArgumentException("Email does not match invitation");
        }

        final User admin = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Inviter not found"));
        final Organization org = Optional.ofNullable(admin.getOrganization())
                .orElseThrow(() -> new IllegalStateException("Organization not found"));

        final User newUser = new User();
        newUser.setFirstname(req.firstname());
        newUser.setLastname(req.lastname());
        newUser.setEmail(req.email());
        newUser.setPassword(passwordEncoder.encode(req.password()));
        newUser.setRole(token.getInviteRole());
        newUser.setMembershipStatus(MembershipStatus.ACTIVE);
        newUser.setOrganization(org);
        newUser.setEmailVerified(true);
        newUser.setPhoneNumber(req.phoneNumber());

        final Address address = new Address();
        final AddressRequest ar = req.address();
        address.setStreet(ar.street());
        address.setCity(ar.city());
        address.setState(ar.state());
        address.setPostalCode(ar.postalCode());
        address.setCountry(ar.country());
        newUser.setAddress(address);

        final User saved = userRepository.save(newUser);

        token.setConsumed(true);
        actionTokenRepository.save(token);

        return new UserResponse(saved.getId(), saved.getEmail(), saved.getRole());
    }
}
