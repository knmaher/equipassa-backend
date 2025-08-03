package com.equipassa.equipassa.security;

import com.equipassa.equipassa.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final UserRole role;
    private final boolean mfaEnabled;
    private final Long organizationId;

    public CustomUserDetails(final Long id, final String email, final String password, final UserRole role, final boolean mfaEnabled, final Long organizationId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.mfaEnabled = mfaEnabled;
        this.organizationId = organizationId;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    public Long getOrganizationId() {
        return organizationId;
    }
}
