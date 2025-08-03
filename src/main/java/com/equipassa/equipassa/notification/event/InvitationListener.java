package com.equipassa.equipassa.notification.event;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.NotificationChannel;
import com.equipassa.equipassa.notification.dto.InvitationEvent;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvitationListener {
    @Value("${equipassa.server:http://localhost:8081}")
    private String equipassaServer;

    private final UserRepository userRepository;
    private final List<NotificationChannel> channels;

    public InvitationListener(final UserRepository userRepository,
                              final List<NotificationChannel> channels) {
        this.userRepository = userRepository;
        this.channels = channels;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInvitation(final InvitationEvent event) {
        final ActionToken token = event.token();
        final User admin = userRepository.findById(token.getUserId()).orElseThrow();

        final String acceptLink = equipassaServer + "/accept?token=" + token.getToken();
        final Map<String, Object> model = new HashMap<>();
        model.put("adminName", admin.getFirstname());
        model.put("organizationName", admin.getOrganization().getName());
        model.put("acceptLink", acceptLink);

        final NotificationPayload payload = new NotificationPayload(
                MailTemplate.INVITATION,
                model,
                new Object[]{admin.getOrganization().getName()}
        );

        final User recipient = new User();
        recipient.setEmail(token.getInviteEmail());
        recipient.setEmailVerified(true);

        channels.forEach(ch -> ch.send(recipient, payload));
    }
}

