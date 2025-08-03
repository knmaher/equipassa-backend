package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUserEmailOrderByCreatedAtDesc(String email);

    @Query("SELECT al FROM AuditLog al WHERE al.eventType = :eventType AND al.createdAt >= :since")
    List<AuditLog> findRecentByEventType(@Param("eventType") String eventType, @Param("since") LocalDateTime since);
}
