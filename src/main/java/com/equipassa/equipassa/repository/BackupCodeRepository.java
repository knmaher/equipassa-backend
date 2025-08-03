package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.BackupCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BackupCodeRepository extends JpaRepository<BackupCode, Long> {
    List<BackupCode> findByUserIdAndUsedFalse(Long userId);

    Optional<BackupCode> findByUserIdAndCodeHashAndUsedFalse(Long userId, String codeHash);
}
