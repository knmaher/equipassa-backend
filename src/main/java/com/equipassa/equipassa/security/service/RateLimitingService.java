package com.equipassa.equipassa.security.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class RateLimitingService {
    private final LettuceBasedProxyManager proxyManager;

    @Autowired
    public RateLimitingService(final RedisClient redisClient) {
        this.proxyManager = LettuceBasedProxyManager.builderFor(redisClient)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();
    }

    public Bucket resolveBucket(final String key, final int capacity, final Duration duration) {
        final Supplier<BucketConfiguration> configSupplier = () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(capacity, duration))
                .build();

        return proxyManager.builder()
                .build(key.getBytes(), configSupplier);
    }
}
