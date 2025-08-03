package com.equipassa.equipassa.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reservations")
public class Reservation extends Auditable {
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "reserved_from", nullable = false)
    private LocalDateTime reservedFrom;

    @Column(name = "reserved_until", nullable = false)
    private LocalDateTime reservedUntil;

    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "late_fee", precision = 10, scale = 2)
    private BigDecimal lateFee;
    
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(final Tool tool) {
        this.tool = tool;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(final ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(final LocalDateTime reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(final LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public LocalDateTime getCheckedOutAt() {
        return checkedOutAt;
    }

    public void setCheckedOutAt(final LocalDateTime checkedOutAt) {
        this.checkedOutAt = checkedOutAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(final LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(final BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Reservation that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTool(), that.getTool()) &&
                Objects.equals(getReservedFrom(), that.getReservedFrom()) &&
                Objects.equals(getReservedUntil(), that.getReservedUntil());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUser(), getTool(), getReservedFrom(), getReservedUntil());
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + getId() +
                ", userId=" + (user != null ? user.getId() : null) +
                ", toolId=" + (tool != null ? tool.getId() : null) +
                ", status=" + status +
                ", reservedFrom=" + reservedFrom +
                ", reservedUntil=" + reservedUntil +
                ", checkedOutAt=" + checkedOutAt +
                ", returnedAt=" + returnedAt +
                ", quantity=" + quantity +
                ", lateFee=" + lateFee +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                ", createdBy=" + getCreatedBy() +
                ", updatedBy=" + getUpdatedBy() +
                ", version=" + getVersion() +
                '}';
    }
}
