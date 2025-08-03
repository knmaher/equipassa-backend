package com.equipassa.equipassa.repository;

import com.equipassa.equipassa.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByTokenHash(String tokenHash);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
