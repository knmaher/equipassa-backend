package com.equipassa.equipassa.notification.event;

import com.equipassa.equipassa.model.Reservation;
import com.equipassa.equipassa.model.Tool;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.NotificationChannel;
import com.equipassa.equipassa.notification.dto.NotificationPayload;
import com.equipassa.equipassa.notification.dto.ReservationConfirmedEvent;
import com.equipassa.equipassa.notification.template.MailTemplate;
import com.equipassa.equipassa.repository.ReservationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

@Component
public class ReservationNotificationListener {

    private final ReservationRepository reservations;
    private final List<NotificationChannel> channels;

    public ReservationNotificationListener(final ReservationRepository reservations, final List<NotificationChannel> channels) {
        this.reservations = reservations;
        this.channels = channels;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservationConfirmed(final ReservationConfirmedEvent confirmedEvent) {

        final Reservation r = reservations.findById(confirmedEvent.reservationId())
                .orElseThrow();

        final User user = r.getUser();
        final Tool tool = r.getTool();

        final Map<String, Object> model = Map.of(
                "reservation", r,
                "tool", tool,
                "user", user,
                "orgName", user.getOrganization().getName(),
                "portalUrl", "https://app.equipassa.com"
        );

        final NotificationPayload payload = new NotificationPayload(
                MailTemplate.RESERVATION_CONFIRMATION,
                model,
                new Object[]{user.getOrganization().getId()}
        );

        channels.stream()
                .filter(ch -> ch.supports(user))
                .forEach(ch -> ch.send(user, payload));
    }
}
