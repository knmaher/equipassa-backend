package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""

                    SELECT COALESCE(SUM(r.quantity), 0) FROM Reservation r
                    WHERE r.tool.id = :toolId
                    AND (r.status = com.equipassa.equipassa.model.ReservationStatus.RESERVED
                               OR r.status = com.equipassa.equipassa.model.ReservationStatus.CHECKED_OUT)
                    AND (r.reservedFrom < :reservedUntil AND r.reservedUntil > :reservedFrom)
            """)
    int sumReservedQuantityForToolInPeriod(@Param("toolId") Long toolId,
                                           @Param("reservedFrom") LocalDateTime reservedFrom,
                                           @Param("reservedUntil") LocalDateTime reservedUntil);
}
