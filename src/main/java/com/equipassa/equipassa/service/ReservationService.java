package com.equipassa.equipassa.service;

import com.equipassa.equipassa.dto.ReservationRequest;
import com.equipassa.equipassa.dto.ReservationResponse;
import com.equipassa.equipassa.model.Reservation;
import com.equipassa.equipassa.model.ReservationStatus;
import com.equipassa.equipassa.model.Tool;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.notification.dto.ReservationConfirmedEvent;
import com.equipassa.equipassa.pipeline.Pipeline;
import com.equipassa.equipassa.repository.ReservationRepository;
import com.equipassa.equipassa.repository.UserRepository;
import com.equipassa.equipassa.reservation.pipeline.ReservationContext;
import com.equipassa.equipassa.reservation.pipeline.ReservationHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class ReservationService {

    private final Pipeline<ReservationContext, ReservationHandler> pipeline;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher events;

    public ReservationService(
            final Pipeline<ReservationContext, ReservationHandler> pipeline,
            final UserRepository userRepository,
            final ReservationRepository reservationRepository,
            ApplicationEventPublisher events
    ) {
        this.pipeline = pipeline;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.events = events;
    }

    @Transactional
    public ReservationResponse reserve(final ReservationRequest reservationRequest, final Long userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Reservation reservationDraft = new Reservation();
        reservationDraft.setUser(user);
        reservationDraft.setStatus(ReservationStatus.RESERVED);

        final ReservationContext reservationContextIn = new ReservationContext(reservationRequest, reservationDraft);
        final ReservationContext reservationContextOut = pipeline.run(reservationContextIn);

        reservationRepository.save(reservationContextOut.draft());

        events.publishEvent(new ReservationConfirmedEvent(reservationContextOut.draft().getId()));

        return mapToResponse(reservationContextOut.draft());
    }

    public ReservationResponse checkout(final Long reservationId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!ReservationStatus.RESERVED.equals(reservation.getStatus())) {
            throw new IllegalArgumentException("Reservation is not in a state that can be checked out.");
        }

        reservation.setCheckedOutAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public ReservationResponse checkIn(final Long reservationId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!ReservationStatus.CHECKED_OUT.equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation is not in a state that can be checked in.");
        }

        final LocalDateTime now = LocalDateTime.now();
        reservation.setReturnedAt(now);
        reservation.setStatus(ReservationStatus.RETURNED);

        if (now.isAfter(reservation.getReservedUntil())) {
            final long minutesLate = Duration.between(reservation.getReservedUntil(), now).toMinutes();
            final BigDecimal lateFee = BigDecimal.valueOf(minutesLate).multiply(new BigDecimal("0.10"));
            reservation.setLateFee(lateFee);
        }

        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public ReservationResponse cancel(final Long reservationId, final Long userId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to cancel this reservation");
        }

        if (!ReservationStatus.RESERVED.equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation cannot be canceled at this stage");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    public ReservationResponse modify(final Long reservationId, final ReservationRequest modificationRequest, final Long userId) {
        final Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to modify this reservation");
        }

        if (!ReservationStatus.RESERVED.equals(reservation.getStatus())) {
            throw new IllegalStateException("Reservation cannot be modified at this stage");
        }

        if (!modificationRequest.reservedFrom().isBefore(modificationRequest.reservedUntil())) {
            throw new IllegalArgumentException("Invalid date range: 'reservedFrom' must be before 'reservedUntil'.");
        }

        final Tool tool = reservation.getTool();
        int alreadyReserved = reservationRepository.sumReservedQuantityForToolInPeriod(
                tool.getId(), modificationRequest.reservedFrom(), modificationRequest.reservedUntil());
        alreadyReserved -= reservation.getQuantity();

        if (alreadyReserved + modificationRequest.quantity() > tool.getQuantityAvailable()) {
            throw new IllegalArgumentException("Modified quantity exceeds available tools for the selected time period");
        }

        reservation.setReservedFrom(modificationRequest.reservedFrom());
        reservation.setReservedUntil(modificationRequest.reservedUntil());
        reservation.setQuantity(modificationRequest.quantity());

        reservationRepository.save(reservation);
        return mapToResponse(reservation);
    }

    private ReservationResponse mapToResponse(final Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getTool().getId(),
                reservation.getStatus(),
                reservation.getReservedFrom(),
                reservation.getReservedUntil(),
                reservation.getCheckedOutAt(),
                reservation.getReturnedAt(),
                reservation.getQuantity()
        );
    }
}
