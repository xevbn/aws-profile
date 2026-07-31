package com.example.awsprofile.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record MemberCreateRequest(
        @NotBlank
        @Size(min = 2, max = 20)
        String name,
        @NotNull
        @Range(min = 20, max = 100)
        int age,
        @NotBlank
        String mbti
) {
}
