package com.equipassa.equipassa.notification.event;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.NotificationChannel;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.dto.OrganizationSubscriptionChangedEvent;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class MembershipRenewedListener {
    private final UserRepository userRepository;
    private final java.util.List<NotificationChannel> channels;

    public MembershipRenewedListener(final UserRepository userRepository,
                                     final java.util.List<NotificationChannel> channels) {
        this.userRepository = userRepository;
        this.channels = channels;
    }

    @TransactionalEventListener
    public void onSubscriptionRenewal(final OrganizationSubscriptionChangedEvent organizationSubscriptionChangedEvent) {
        final User user = userRepository.findById(organizationSubscriptionChangedEvent.userId()).orElseThrow();
        final Map<String, Object> model = Map.of(
                "user", user,
                "newExpiry", user.getSubscriptionExpiryDate()
        );

        final NotificationPayload notificationPayload = new NotificationPayload(
                MailTemplate.MEMBERSHIP_RENEWAL, model, new Object[]{DateTimeFormatter.ofPattern("d MM yyyy")}
        );
        channels.stream()
                .filter(ch -> ch.supports(user))
                .forEach(ch -> ch.send(user, notificationPayload));
    }
}
