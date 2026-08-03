package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.FileUploadException;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    private final S3Template s3Template;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file) {
        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());

            return key;
        } catch (IOException e) {
            throw new FileUploadException("S3 파일 업로드 중 오류가 발생했습니다. 파일명: " + file.getOriginalFilename());
        }
    }

    public URL download(String key) {
        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION);
    }
}
