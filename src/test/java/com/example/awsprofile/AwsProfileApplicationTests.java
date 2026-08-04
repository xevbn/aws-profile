package com.example.awsprofile;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {S3AutoConfiguration.class})
class AwsProfileApplicationTests {
    @MockitoBean
    private S3Template s3Template;

    @Test
    void contextLoads() {
    }

}
