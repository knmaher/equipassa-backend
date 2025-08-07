package com.equipassa.equipassa.util;

import jakarta.servlet.http.HttpServletResponse;

import java.time.Duration;

public class CookieUtils {
    public static void addHttpOnlyCookie(
            final HttpServletResponse response,
            final String name,
            final String value,
            final Duration maxAge,
            final String path,
            final boolean secure,
            final String sameSite
    ) {
        final StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value).append(";")
                .append("Max-Age=").append(maxAge.getSeconds()).append(";")
                .append("Path=").append(path).append(";")
                .append("HttpOnly;")
                .append("SameSite=").append(sameSite).append(";");
        if (secure) sb.append("Secure;");
        response.addHeader("Set-Cookie", sb.toString());
    }

    public static void clearCookie(final HttpServletResponse response, final String name, final String path) {
        addHttpOnlyCookie(response, name, "", Duration.ZERO, path, true, "Strict");
    }
}
