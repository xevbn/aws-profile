package com.example.awsprofile.domain.member.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class S3TestSupport {
    //컨테이너 선언
    @Container
    protected static GenericContainer<?> minio = new GenericContainer<>("minio/minio:latest")
            .withEnv("MINIO_ROOT_USER", "minio")
            .withEnv("MINIO_ROOT_PASSWORD", "minio123")
            .withCommand("server", "/data")
            .withExposedPorts(9000);

    //환경 설정 값 추가
    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.cloud.aws.credentials.access-key",
                () -> "minio"
        );

        registry.add(
                "spring.cloud.aws.credentials.secret-key",
                () -> "minio123"
        );

        registry.add(
                "spring.cloud.aws.region.static",
                () -> "ap-northeast-2"
        );

        registry.add(
                "spring.cloud.aws.s3.bucket",
                () -> "test"
        );

        registry.add(
                "spring.cloud.aws.s3.endpoint",
                () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000)
        );
    }
}
