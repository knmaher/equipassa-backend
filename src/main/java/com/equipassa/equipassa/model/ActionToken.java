package com.equipassa.equipassa.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_tokens")
public class ActionToken extends Auditable {

    @Column(name = "token", unique = true, nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ActionTokenType type;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "consumed", nullable = false)
    private boolean consumed = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private TokenVisibility visibility;

    @Column(name = "invite_email")
    private String inviteEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "invite_role")
    private UserRole inviteRole;

    public ActionToken() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public ActionTokenType getType() {
        return type;
    }

    public void setType(final ActionTokenType type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(final LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void setConsumed(final boolean consumed) {
        this.consumed = consumed;
    }

    public TokenVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(final TokenVisibility visibility) {
        this.visibility = visibility;
    }

    public String getInviteEmail() {
        return inviteEmail;
    }

    public void setInviteEmail(final String inviteEmail) {
        this.inviteEmail = inviteEmail;
    }

    public UserRole getInviteRole() {
        return inviteRole;
    }

    public void setInviteRole(final UserRole inviteRole) {
        this.inviteRole = inviteRole;
    }
}
