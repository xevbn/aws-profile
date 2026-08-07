package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.FileUploadException;
import com.example.awsprofile.domain.common.exception.S3CommunicationException;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;

import java.io.IOException;
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
        } catch (SdkException | S3Exception e) {
            throw new S3CommunicationException("S3 파일 업로드 에러 발생");
        }
    }

    public void delete(String key) {
        try {
            s3Template.deleteObject(bucket, key);
        } catch (SdkException | S3Exception e) {
            throw new S3CommunicationException("S3 삭제 도중 에러 발생");
        }
    }
}
