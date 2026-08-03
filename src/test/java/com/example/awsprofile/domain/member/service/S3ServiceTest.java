package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.FileUploadException;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {
    @Mock
    private S3Template s3Template;
    @InjectMocks
    private S3Service s3Service;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

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
        verify(s3Template, times(1)).upload(eq(bucket), any(String.class), any(ByteArrayInputStream.class));
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
    void download() throws MalformedURLException {
        //given
        String key = "test-key";
        URL url = new URL("https://example.com");
        given(s3Template.createSignedGetURL(any(), anyString(), any(Duration.class))).willReturn(url);

        //when
        s3Service.download(key);

        //then
        verify(s3Template, times(1)).createSignedGetURL(any(), anyString(), any(Duration.class));
    }
}