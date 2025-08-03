package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.dto.ReservationRequest;
import com.equipassa.equipassa.dto.ReservationResponse;
import com.equipassa.equipassa.security.CurrentUser;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.equipassa.equipassa.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN', 'STAFF', 'USER')")
    public ResponseEntity<ReservationResponse> reserve(
            @CurrentUser final CustomUserDetails userDetails,
            @RequestBody @Valid final ReservationRequest reservationRequest
    ) {
        final ReservationResponse response = reservationService.reserve(reservationRequest, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/check-out")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN', 'STAFF')")
    public ResponseEntity<ReservationResponse> checkout(@PathVariable final Long reservationId) {
        final ReservationResponse response = reservationService.checkout(reservationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/check-in")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORG_ADMIN', 'STAFF')")
    public ResponseEntity<ReservationResponse> checkIn(@PathVariable final Long reservationId) {
        final ReservationResponse response = reservationService.checkIn(reservationId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponse> cancel(
            @CurrentUser final CustomUserDetails userDetails,
            @PathVariable final Long reservationId
    ) {
        final ReservationResponse response = reservationService.cancel(reservationId, userDetails.getId());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reservationId}/modify")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservationResponse> modify(
            @CurrentUser final CustomUserDetails userDetails,
            @PathVariable final Long reservationId,
            @RequestBody @Valid final ReservationRequest modificationRequest
    ) {
        final ReservationResponse response = reservationService.modify(reservationId, modificationRequest, userDetails.getId());
        return ResponseEntity.ok(response);
    }
}
