package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.NotFoundException;
import com.example.awsprofile.domain.member.dto.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.MemberResponse;
import com.example.awsprofile.domain.member.entity.Member;
import com.example.awsprofile.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void createMember(MemberCreateRequest createRequest) {
        Member member = Member.create(
                createRequest.name(),
                createRequest.age(),
                createRequest.mbti()
        );

        memberRepository.save(member);
    }
}