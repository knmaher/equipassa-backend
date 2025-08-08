package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.dto.UserRequest;
import com.equipassa.equipassa.dto.UserResponse;
import com.equipassa.equipassa.exception.EmailExistsException;
import com.equipassa.equipassa.exception.OrganizationExistsException;
import com.equipassa.equipassa.model.*;
import com.equipassa.equipassa.notification.dto.PasswordResetEvent;
import com.equipassa.equipassa.notification.dto.RegistrationVerificationEvent;
import com.equipassa.equipassa.repository.OrganizationRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.security.dto.OrgRegistrationRequest;
import com.equipassa.equipassa.security.dto.OrgUserResponse;
import com.equipassa.equipassa.service.token.ActionTokenContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${jwt.password-reset.expiration-minutes:60}")
    private int passwordResetMinutes;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final AuthenticationManager authenticationManager;
    private final MfaService mfaService;
    private final BackupCodeService backupCodeService;
    private final OrganizationRepository organizationRepository;
    private final ActionTokenContext actionTokenContext;
    private final ApplicationEventPublisher events;
    private final ActionTokenContext tokenContext;

    public AuthService(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final AuditLogService auditLogService,
            final AuthenticationManager authenticationManager,
            final MfaService mfaService,
            final BackupCodeService backupCodeService,
            final OrganizationRepository organizationRepository,
            final ActionTokenContext actionTokenContext,
            final ApplicationEventPublisher events,
            final ActionTokenContext tokenContext
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
        this.authenticationManager = authenticationManager;
        this.mfaService = mfaService;
        this.backupCodeService = backupCodeService;
        this.organizationRepository = organizationRepository;
        this.actionTokenContext = actionTokenContext;
        this.events = events;
        this.tokenContext = tokenContext;
    }

    @Transactional
    public OrgUserResponse createOrganizationAndAdmin(final OrgRegistrationRequest request) {
        final boolean isOrgNameExist = organizationRepository.existsByName(request.organizationName());
        if (isOrgNameExist) {
            throw new OrganizationExistsException("Organization name already exist: " + request.organizationName());
        }
        final boolean isAdminEmailExist = userRepository.existsByEmail(request.adminEmail());
        if (isAdminEmailExist) {
            throw new EmailExistsException("Email already registered: " + request.adminEmail());
        }

        final Address address = new Address();
        address.setStreet(request.addressRequest().street());
        address.setCity(request.addressRequest().city());
        address.setState(request.addressRequest().state());
        address.setPostalCode(request.addressRequest().postalCode());
        address.setCountry(request.addressRequest().country());

        final Organization organization = new Organization();
        organization.setName(request.organizationName());
        organization.setSubscriptionTier(request.subscriptionTier());
        organization.setAddress(address);

        final Organization savedOrg = organizationRepository.save(organization);

        final User adminUser = new User();
        adminUser.setFirstname(request.adminFirstname());
        adminUser.setLastname(request.adminLastname());
        adminUser.setEmail(request.adminEmail());
        adminUser.setPassword(passwordEncoder.encode(request.adminPassword()));
        adminUser.setRole(UserRole.ORG_ADMIN);
        adminUser.setMembershipStatus(MembershipStatus.ACTIVE);
        adminUser.setOrganization(savedOrg);
        adminUser.setEmailVerified(false);

        final User savedUser = userRepository.save(adminUser);

        events.publishEvent(new RegistrationVerificationEvent(savedUser.getId()));

        return new OrgUserResponse(
                savedOrg.getId(),
                savedOrg.getName(),
                savedUser.getId(),
                savedUser.getEmail()
        );
    }

    public UserResponse register(final UserRequest request, final String clientIp) {
        if (userRepository.existsByEmail(request.email())) {
            auditLogService.logSecurityEvent(
                    request.email(),
                    "REGISTRATION_ATTEMPT",
                    "Duplicate email registration attempt",
                    clientIp
            );
            throw new EmailExistsException("Email already registered");
        }

        final User user = createUserFromRequest(request);
        final User savedUser = userRepository.save(user);

        auditLogService.logSecurityEvent(savedUser.getEmail(),
                "REGISTRATION_SUCCESS", "User registered successfully", clientIp);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public void auditLoginSuccess(final String email, final String clientIp) {
        auditLogService.logSecurityEvent(
                email,
                "LOGIN_SUCCESS",
                "User logged in successfully",
                clientIp
        );
    }

    @Transactional
    public String verifyEmail(final String token) {
        final ActionToken actionToken = actionTokenContext.validateToken(ActionTokenType.EMAIL_VERIFICATION, token);
        if (actionToken == null) {
            throw new BadCredentialsException("Invalid or expired token.");
        }
        final User user = userRepository.findById(actionToken.getUserId())
                .orElseThrow(() -> new BadCredentialsException("Invalid token: user not found."));
        user.setEmailVerified(true);
        userRepository.save(user);
        actionTokenContext.consumeToken(ActionTokenType.EMAIL_VERIFICATION, token);
        return "Email verified successfully.";
    }

    public void requestPasswordReset(final String email) {
        final User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("No user with email"));
        final String actionToken = tokenContext.generateToken(
                ActionTokenType.PASSWORD_RESET, u.getId(), passwordResetMinutes
        ).getToken();
        events.publishEvent(new PasswordResetEvent(u.getId(), actionToken));
    }

    public void confirmPasswordReset(final String token, final String newPassword) {
        final ActionToken actionToken = tokenContext.validateToken(
                ActionTokenType.PASSWORD_RESET, token
        );
        if (actionToken == null) throw new BadCredentialsException("Invalid or expired reset token");
        final User u = userRepository.findById(actionToken.getUserId())
                .orElseThrow();
        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
        tokenContext.consumeToken(ActionTokenType.PASSWORD_RESET, token);
    }

    private User createUserFromRequest(final UserRequest request) {
        final User user = new User();
        user.setFirstname(request.firstname());
        user.setLastname(request.lastname());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(UserRole.USER);
        user.setMembershipStatus(MembershipStatus.ACTIVE);
        user.setPhoneNumber(request.phoneNumber());

        final Address address = new Address();
        address.setStreet(request.address().street());
        address.setCity(request.address().city());
        address.setState(request.address().state());
        address.setPostalCode(request.address().postalCode());
        address.setCountry(request.address().country());

        user.setAddress(address);
        return user;
    }
}
