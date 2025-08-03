package com.equipassa.equipassa.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {
    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public TokenBlacklist(final String tokenHash, final LocalDateTime expiresAt) {
        this.tokenHash = tokenHash;
        this.blacklistedAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }

    public TokenBlacklist() {

    }

    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(final LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
