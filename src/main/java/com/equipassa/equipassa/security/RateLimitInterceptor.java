package com.equipassa.equipassa.security;

import com.equipassa.equipassa.exception.TooManyRequestsException;
import com.equipassa.equipassa.security.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimitingService rateLimitingService;

    public RateLimitInterceptor(final RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response, @NonNull final Object handler) {
        if (handler instanceof final HandlerMethod handlerMethod) {
            final RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
            if (rateLimit != null) {
                final String key = rateLimit.bucketKey() + "-" + request.getRemoteAddr();
                final Bucket bucket = rateLimitingService.resolveBucket(
                        key,
                        rateLimit.capacity(),
                        Duration.ofSeconds(rateLimit.duration())
                );
                if (!bucket.tryConsume(1)) {
                    throw new TooManyRequestsException("Too many requests from " + request.getRemoteAddr());
                }
            }
        }
        return true;
    }
}
