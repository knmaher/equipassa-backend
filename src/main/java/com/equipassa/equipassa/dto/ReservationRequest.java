package com.equipassa.equipassa.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull(message = "Tool ID is required") Long toolId,
        @NotNull(message = "Reserved from date is required")
        @Future(message = "Reserved from must be a future date") LocalDateTime reservedFrom,
        @NotNull(message = "Reserved until date is required")
        @Future(message = "Reserved until must be a future date") LocalDateTime reservedUntil,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be at least 1") Integer quantity
) {
}
