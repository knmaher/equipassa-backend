package com.equipassa.equipassa.security.service;

import com.equipassa.equipassa.model.RefreshToken;
import com.equipassa.equipassa.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refreshExpiration}") // e.g., 604800000 for 7 days (in milliseconds)
    private long refreshExpiration;

    public RefreshTokenService(final RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(final Long userId) {
        final RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setIssuedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        token.setToken(generateRandomToken());
        return refreshTokenRepository.save(token);
    }

    private String generateRandomToken() {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] tokenBytes = new byte[64];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    public Optional<RefreshToken> findByToken(final String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void revokeToken(final String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    public boolean isTokenExpired(final RefreshToken token) {
        return token.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
