package com.example.awsprofile.domain.common.exception;

public class S3CommunicationException extends RuntimeException {
    public S3CommunicationException(String message) {
        super(message);
    }
}
