package com.equipassa.equipassa;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ExtendWith(SpringExtension.class)
public abstract class AbstractIntegrationTest {

    @SuppressWarnings("resource")
    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("toollibrary")
            .withUsername("equipassa")
            .withPassword("equipassa123");

    @SuppressWarnings("resource")
    @Container
    public static GenericContainer<?> minioContainer = new GenericContainer<>("minio/minio:latest")
            .withExposedPorts(9000, 9001)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin123")
            .withCommand("server", "/data", "--console-address", ":9001");

    @SuppressWarnings("resource")
    @Container
    public static GenericContainer<?> mailhogContainer = new GenericContainer<>("mailhog/mailhog")
            .withExposedPorts(1025, 8025);

    @SuppressWarnings("resource")
    @Container
    private static final GenericContainer<?> redisContainer =
            new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine"))
                    .withExposedPorts(6379);

    @DynamicPropertySource
    public static void overrideProperties(final DynamicPropertyRegistry registry) {
        // Override PostgreSQL datasource properties for Spring Boot
        registry.add("spring.datasource.url", () ->
                "jdbc:postgresql://" + postgresContainer.getHost() + ":" + postgresContainer.getMappedPort(5432) + "/" + postgresContainer.getDatabaseName());
        registry.add("spring.datasource.username", () -> postgresContainer.getUsername());
        registry.add("spring.datasource.password", () -> postgresContainer.getPassword());

        // Configure AWS S3 to point to MinIO
        registry.add("aws.s3.endpoint", () ->
                "http://" + minioContainer.getHost() + ":" + minioContainer.getMappedPort(9000));
        registry.add("aws.s3.bucket", () -> "equipassa-bucket");
        registry.add("aws.s3.accessKey", () -> "minioadmin");
        registry.add("aws.s3.secretKey", () -> "minioadmin123");
        registry.add("aws.s3.region", () -> "hamburg");

        // Optionally, override mail properties for Mailhog (if testing email sending)
        registry.add("spring.mail.host", () -> mailhogContainer.getHost());
        registry.add("spring.mail.port", () -> mailhogContainer.getMappedPort(1025));

        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", redisContainer::getFirstMappedPort);
    }
}
