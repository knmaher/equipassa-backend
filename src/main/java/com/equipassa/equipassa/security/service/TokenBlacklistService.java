package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.TokenBlacklist;
import com.equipassa.equipassa.repository.TokenBlacklistRepository;
import com.equipassa.equipassa.util.JwtUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenBlacklistService {
    private final JwtUtil jwtUtil;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenBlacklistService(final JwtUtil jwtUtil, final TokenBlacklistRepository tokenBlacklistRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Scheduled(fixedRate = 3600000) // Cleanup every hour
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    public void blacklistToken(final String token) {
        final String tokenHash = jwtUtil.hashToken(token);
        final LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

        tokenBlacklistRepository.save(new TokenBlacklist(
                tokenHash,
                expiresAt
        ));
    }

    public boolean isTokenBlacklisted(final String token) {
        return tokenBlacklistRepository.existsByTokenHash(jwtUtil.hashToken(token));
    }

}
