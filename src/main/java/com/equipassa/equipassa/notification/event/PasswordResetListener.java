package com.equipassa.equipassa.notification.event;

import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.NotificationChannel;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.dto.PasswordResetEvent;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

@Component
public class PasswordResetListener {
    @Value("${equipassa.server:http://localhost:8081}")
    private String equipassaServer;

    private final UserRepository userRepository;
    private final List<NotificationChannel> channels;

    public PasswordResetListener(final UserRepository userRepository, final List<NotificationChannel> channels) {
        this.userRepository = userRepository;
        this.channels = channels;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPasswordResetRequested(final PasswordResetEvent passwordResetEvent) {
        final User user = userRepository.findById(passwordResetEvent.userId()).orElseThrow();
        final String link = equipassaServer + "/api/auth/password-reset/confirm?token=" + passwordResetEvent.token();

        final Map<String, Object> model = Map.of(
                "user", user,
                "resetLink", link
        );

        final NotificationPayload p = new NotificationPayload(
                MailTemplate.PASSWORD_RESET,
                model,
                null
        );
        channels.stream()
                .filter(ch -> ch.supports(user))
                .forEach(ch -> ch.send(user, p));
    }
}
