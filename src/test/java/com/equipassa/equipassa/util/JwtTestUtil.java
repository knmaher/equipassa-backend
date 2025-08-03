package com.equipassa.equipassa.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTestUtil {
    private static final String JWT_SECRET = "oanUY3TWtuwJKukAndKwb+DLt8/QtjS41zRuJJ8Ql8k=";

    public static String generateTestJwtToken(
            final String email,
            final Long userId,
            final String role,
            final Long organizationId,      // <- new parameter
            final int expirationMinutes) {

        final Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("role", role)
                .claim("mfaEnabled", false)
                .claim("organizationId", organizationId)   // <- use it here
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(getSignInKey())
                .compact();
    }

    private static SecretKey getSignInKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
