package com.equipassa.equipassa.dto;

import com.equipassa.equipassa.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long userId,
        Long toolId,
        ReservationStatus status,
        LocalDateTime reservedFrom,
        LocalDateTime reservedUntil,
        LocalDateTime checkedOutAt,
        LocalDateTime returnedAt,
        Integer quantity
) {
}
