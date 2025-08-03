package com.equipassa.equipassa.util;

import com.equipassa.equipassa.model.UserRole;
import com.equipassa.equipassa.security.CustomUserDetails;
import com.google.common.hash.Hashing;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.extendedExpiration}")
    private long jwtExtendedExpiration;

    public long getExpirationTime() {
        return jwtExpiration;
    }

    public long getExtendedExpiration() {
        return jwtExtendedExpiration;
    }

    public String generateToken(final CustomUserDetails userDetails) {
        return generateToken(userDetails, false);
    }

    public String generateToken(final CustomUserDetails userDetails, final boolean extended) {
        final long expirationTime = extended ? jwtExtendedExpiration : jwtExpiration;
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", userDetails.getId())
                .claim("role", userDetails.getRole())
                .claim("mfaEnabled", userDetails.isMfaEnabled())
                .claim("organizationId", userDetails.getOrganizationId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey())
                .compact();
    }

    public CustomUserDetails extractUserDetails(final String token) {
        final Claims claims = extractAllClaims(token);
        return new CustomUserDetails(
                claims.get("userId", Long.class),
                claims.getSubject(),
                null, // Password not stored in JWT
                UserRole.valueOf(claims.get("role", String.class)),
                claims.get("mfaEnabled", Boolean.class),
                claims.get("organizationId", Long.class)
        );
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(final String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String hashToken(final String token) {
        return Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
    }

    public boolean validateToken(final String token) {
        try {
            final Claims claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Corrected validation logic: Check if token is NOT expired
            // Removed inverted "mfa_required" check based on problem description
            return !claims.getExpiration().before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
