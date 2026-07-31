package com.example.awsprofile.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    int age;
    String mbti;
    String profile;

    public static Member create(String name, int age, String mbti) {
        Member member = new Member();
        member.name = name;
        member.age = age;
        member.mbti = mbti;

        return member;
    }

    public void saveProfile(String profile) {
        this.profile = profile;
    }
}
