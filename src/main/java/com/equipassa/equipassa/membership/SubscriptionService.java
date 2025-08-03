package com.equipassa.equipassa.membership;

import com.equipassa.equipassa.membership.dto.MembershipRenewalResponse;
import com.equipassa.equipassa.membership.dto.OrgSubscriptionResponse;
import com.equipassa.equipassa.model.Organization;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.model.UserRole;
import com.equipassa.equipassa.notification.dto.OrganizationSubscriptionChangedEvent;
import com.equipassa.equipassa.repository.OrganizationRepository;
import com.equipassa.equipassa.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionService {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final RenewalContext renewals;
    private final ApplicationEventPublisher events;

    public SubscriptionService(
            final UserRepository userRepository,
            final OrganizationRepository organizationRepository,
            final RenewalContext renewals,
            final ApplicationEventPublisher events
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.renewals = renewals;
        this.events = events;
    }

    public OrgSubscriptionResponse renewOrg(final Long orgId, final int extensionMonths) {
        final Organization org = organizationRepository.findById(orgId).orElseThrow();
        final LocalDate newExpiry = renewals.renew(org).plusMonths(extensionMonths);

        org.setSubscriptionExpiryDate(newExpiry);
        organizationRepository.save(org);

        userRepository.findFirstByOrganizationIdAndRole(orgId, UserRole.ORG_ADMIN)
                .ifPresent(admin ->
                        events.publishEvent(new OrganizationSubscriptionChangedEvent(admin.getId(), newExpiry))
                );

        return new OrgSubscriptionResponse(orgId, org.getSubscriptionTier(), newExpiry);
    }

    public MembershipRenewalResponse renewUser(final Long userId, final int extensionMonths) {
        final User user = userRepository.findById(userId).orElseThrow();
        final LocalDate newExpiry = renewals.renew(user).plusMonths(extensionMonths);

        user.setSubscriptionExpiryDate(newExpiry);
        userRepository.save(user);

        events.publishEvent(new OrganizationSubscriptionChangedEvent(userId, newExpiry));

        return new MembershipRenewalResponse(userId, newExpiry);
    }
}
