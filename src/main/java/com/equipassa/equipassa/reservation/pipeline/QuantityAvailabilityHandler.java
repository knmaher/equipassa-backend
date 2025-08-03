package com.equipassa.equipassa.reservation.pipeline;

import com.equipassa.equipassa.dto.ReservationRequest;
import com.equipassa.equipassa.model.Reservation;
import com.equipassa.equipassa.repository.ReservationRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class QuantityAvailabilityHandler implements ReservationHandler {

    private final ReservationRepository reservationRepository;

    public QuantityAvailabilityHandler(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public ReservationContext handle(final ReservationContext context) {
        final Reservation draft = context.draft();
        final ReservationRequest reservationRequest = context.request();

        final int alreadyReserved = reservationRepository.sumReservedQuantityForToolInPeriod(
                draft.getTool().getId(),
                reservationRequest.reservedFrom(),
                reservationRequest.reservedUntil());

        if (alreadyReserved + reservationRequest.quantity() > draft.getTool().getQuantityAvailable()) {
            throw new IllegalArgumentException("Requested quantity exceeds available tools");
        }

        draft.setQuantity(reservationRequest.quantity());
        return context;
    }
}
