package com.equipassa.equipassa.reservation.pipeline;

import com.equipassa.equipassa.dto.ReservationRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class DateRangeValidationHandler implements ReservationHandler {

    @Override
    public ReservationContext handle(final ReservationContext reservationContext) {

        final ReservationRequest reservationRequest = reservationContext.request();
        
        if (!reservationRequest.reservedFrom().isBefore(reservationRequest.reservedUntil())) {
            throw new IllegalArgumentException("'reservedFrom' must be before 'reservedUntil'");
        }

        reservationContext.draft().setReservedFrom(reservationRequest.reservedFrom());
        reservationContext.draft().setReservedUntil(reservationRequest.reservedUntil());
        return reservationContext;
    }
}
