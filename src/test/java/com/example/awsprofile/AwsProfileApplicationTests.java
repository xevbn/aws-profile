package com.example.awsprofile;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class AwsProfileApplicationTests {
    @MockitoBean
    private S3Template s3Template;

    @Test
    void contextLoads() {
    }

}
