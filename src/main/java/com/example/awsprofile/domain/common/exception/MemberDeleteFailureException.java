package com.example.awsprofile.domain.common.exception;

public class MemberDeleteFailureException extends RuntimeException {
    public MemberDeleteFailureException(String message) {
        super(message);
    }
}
