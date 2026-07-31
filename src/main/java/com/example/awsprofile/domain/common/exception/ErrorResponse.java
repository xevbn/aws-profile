package com.example.awsprofile.domain.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
    String status,
    int code,
    String message
) {
    public static ErrorResponse of(HttpStatus httpStatus, String message) {
        return new ErrorResponse(
                httpStatus.name(),
                httpStatus.value(),
                message
        );
    }
}
