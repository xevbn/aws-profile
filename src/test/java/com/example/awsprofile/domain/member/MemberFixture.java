package com.example.awsprofile.domain.member;

import com.example.awsprofile.domain.member.entity.Member;

public class MemberFixture {
    public static Member create(String name, int age, String mbti) {
        return Member.create(name, age, mbti);
    }
}
