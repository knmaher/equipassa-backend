package com.equipassa.equipassa.service;

import com.equipassa.equipassa.security.CustomUserDetails;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;

@Service
public class S3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3Service.class);

    private final String bucketName;
    private final S3Template s3Template;
    private final S3Presigner presigner;

    public S3Service(final S3Template s3Template,
                     @Value("${aws.s3.bucket}") final String bucketName, final S3Presigner presigner) {
        this.s3Template = s3Template;
        this.bucketName = bucketName;
        this.presigner = presigner;
    }

    private String getCurrentOrgPrefix() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof final CustomUserDetails userDetails)) {
            throw new IllegalStateException("No authenticated user found");
        }
        final Long orgId = userDetails.getOrganizationId();
        if (orgId == null) {
            throw new IllegalStateException("No organization ID found for user");
        }
        return "org-" + orgId + "/";
    }

    public String uploadObject(final String key, final InputStream inputStream) {
        final String prefix = getCurrentOrgPrefix();
        final String fullKey = prefix + key;
        LOGGER.debug("Uploading object to bucket: {}, key: {}", bucketName, fullKey);
        try {
            s3Template.upload(bucketName, fullKey, inputStream);
            LOGGER.info("Uploaded object to bucket: {}, key: {}", bucketName, fullKey);
            return fullKey;
        } catch (final Exception e) {
            LOGGER.error("Failed to upload object: {}", fullKey, e);
            throw new RuntimeException("Upload failed", e);
        }
    }

    public InputStream downloadObject(final String key) {
        final String prefix = getCurrentOrgPrefix();
        final String fullKey = prefix + key;
        LOGGER.debug("Downloading object from bucket: {}, key: {}", bucketName, fullKey);
        try {
            final S3Resource s3Resource = s3Template.download(bucketName, fullKey);
            final InputStream inputStream = s3Resource.getInputStream();
            LOGGER.info("Downloaded object from bucket: {}, key: {}", bucketName, fullKey);
            return inputStream;
        } catch (final Exception e) {
            LOGGER.error("Failed to download object: {}", fullKey, e);
            throw new RuntimeException("Download failed", e);
        }
    }

    public void deleteObject(final String key) {
        final String prefix = getCurrentOrgPrefix();
        final String fullKey = prefix + key;
        LOGGER.debug("Deleting object from bucket: {}, key: {}", bucketName, fullKey);
        try {
            s3Template.deleteObject(bucketName, fullKey);
            LOGGER.info("Deleted object from bucket: {}, key: {}", bucketName, fullKey);
        } catch (final Exception e) {
            LOGGER.error("Failed to delete object: {}", fullKey, e);
            throw new RuntimeException("Deletion failed", e);
        }
    }

    public String presignedGetUrl(final String bucket, final String key, final Duration ttl) {
        final var getReq = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        final var presignReq = GetObjectPresignRequest.builder()
                .getObjectRequest(getReq)
                .signatureDuration(ttl)
                .build();

        return presigner.presignGetObject(presignReq).url().toString();
    }

    public void deleteObjects(final List<String> keys) {
        for (final String key : keys) {
            deleteObject(key);
        }
    }
}
