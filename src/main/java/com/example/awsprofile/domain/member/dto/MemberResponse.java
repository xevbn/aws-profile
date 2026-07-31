package com.example.awsprofile.domain.member.dto;

import com.example.awsprofile.domain.member.entity.Member;
import jakarta.annotation.Nullable;

public record MemberResponse(
        Long id,
        String name,
        int age,
        String mbti,
        @Nullable
        String profile
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti(),
                member.getProfile()
        );
    }
}
