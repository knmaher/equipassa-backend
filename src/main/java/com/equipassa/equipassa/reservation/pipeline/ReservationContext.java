package com.equipassa.equipassa.reservation.pipeline;

import com.equipassa.equipassa.dto.ReservationRequest;
import com.equipassa.equipassa.model.Reservation;

public record ReservationContext(
        ReservationRequest request,
        Reservation draft
) {
}
