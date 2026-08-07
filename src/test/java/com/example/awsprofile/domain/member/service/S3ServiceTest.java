package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.FileUploadException;
import com.example.awsprofile.domain.common.exception.S3CommunicationException;
import com.example.awsprofile.domain.member.support.S3TestSupport;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class S3ServiceTest extends S3TestSupport {
    @Autowired
    private S3Template s3Template;
    @Autowired
    private S3Service s3Service;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @BeforeEach
    void setup() {
        if(!s3Template.bucketExists("test")) {
            s3Template.createBucket("test");
        }
    }

    @Test
    @DisplayName("s3 업로드 테스트 - 성공")
    void upload_success() throws IOException {
        //given
        String key = "test-key";
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);

        //when
        s3Service.upload(mockMultipartFile);

        //then
        assertNotNull(s3Template.download(bucket, key));
    }

    @Test
    @DisplayName("S3 업로드 테스트 - S3 업로드 중 오류 발생")
    void upload_failure() throws IOException {
        //given
        MultipartFile mockFile = mock(MultipartFile.class);
        given(mockFile.getOriginalFilename()).willReturn("test.png");
        given(mockFile.getInputStream()).willThrow(new IOException("Stream error"));

        //when&then
        assertThrows(FileUploadException.class, () -> s3Service.upload(mockFile));
    }

    @Test
    @DisplayName("S3 데이터 삭제 테스트 - 성공")
    void delete_success() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        String key = s3Service.upload(mockMultipartFile);

        //when
        s3Service.delete(key);

        //then
        assertFalse(s3Template.objectExists(bucket, key));
    }

    @Test
    @DisplayName("S3 bucket이 없음")
    void bucket_not_found_test() throws IOException {
        //given
        String key = "test-key";
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        ReflectionTestUtils.setField(s3Service, "bucket", "nothing");

        //when&then
        assertThrows(S3CommunicationException.class, () -> s3Service.upload(new MockMultipartFile("file", "test".getBytes())));
    }

    @Test
    @DisplayName("S3 연결 불가")
    void s3_not_reachable_test() throws IOException {
        //given
        String key = "test-key";
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        ReflectionTestUtils.setField(s3Service, "bucket", "nothing");

        //when
        minio.stop();

        //then
        assertThrows(S3CommunicationException.class, () -> s3Service.upload(new MockMultipartFile("file", "test".getBytes())));
    }
}