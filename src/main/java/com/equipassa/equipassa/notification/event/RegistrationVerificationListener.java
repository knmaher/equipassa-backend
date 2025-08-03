package com.equipassa.equipassa.notification.event;

import com.equipassa.equipassa.model.ActionToken;
import com.equipassa.equipassa.model.ActionTokenType;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.NotificationChannel;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.dto.RegistrationVerificationEvent;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.service.token.ActionTokenContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationVerificationListener {
    @Value("${equipassa.server}")
    private String serverBaseUrl;

    private final UserRepository userRepository;
    private final ActionTokenContext tokenContext;
    private final List<NotificationChannel> channels;

    public RegistrationVerificationListener(final UserRepository userRepository, final ActionTokenContext tokenContext, final List<NotificationChannel> channels) {
        this.userRepository = userRepository;
        this.tokenContext = tokenContext;
        this.channels = channels;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRegistrationVerified(final RegistrationVerificationEvent verificationEvent) {

        final User user = userRepository.findById(verificationEvent.userId())
                .orElseThrow();

        final ActionToken actionToken = tokenContext.generateToken(
                ActionTokenType.EMAIL_VERIFICATION,
                user.getId(),
                60
        );
        
        final String verificationLink = serverBaseUrl
                + "/api/auth/verify-email?token="
                + actionToken.getToken();

        final Map<String, Object> model = new HashMap<>();
        model.put("adminName", user.getFirstname());
        model.put("organizationName", user.getOrganization().getName());
        model.put("verificationLink", verificationLink);

        final NotificationPayload payload = new NotificationPayload(
                MailTemplate.REGISTRATION_VERIFICATION,
                model,
                null
        );

        channels.forEach(ch -> ch.send(user, payload));
    }
}
