package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.AuditLog;
import com.equipassa.equipassa.model.User;
import com.equipassa.equipassa.repository.AuditLogRepository;
import com.equipassa.equipassa.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLogService(final AuditLogRepository auditLogRepository, final UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
    }

    public void logSecurityEvent(final String email, final String eventType, final String description, final String ipAddress) {
        final User user = userRepository.findByEmail(email).orElse(null);

        final AuditLog log = new AuditLog();
        log.setEventType(eventType);
        log.setDescription(description);
        log.setUser(user);
        log.setIpAddress(ipAddress);

        auditLogRepository.save(log);
    }
}
