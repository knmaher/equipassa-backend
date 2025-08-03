package com.equipassa.equipassa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "backup_codes")
public class BackupCode extends Auditable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String codeHash; // Store hashed backup code

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(final String codeHash) {
        this.codeHash = codeHash;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(final boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
