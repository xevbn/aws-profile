package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.NotFoundException;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
import com.example.awsprofile.domain.member.dto.response.ProfileImageResponse;
import com.example.awsprofile.domain.member.entity.Member;
import com.example.awsprofile.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public void createMember(MemberCreateRequest createRequest) {
        Member member = Member.create(
                createRequest.name(),
                createRequest.age(),
                createRequest.mbti()
        );

        memberRepository.save(member);
    }

    public MemberResponse findMember(Long id) {
        Member found = getMember(id);

        return MemberResponse.from(found);
    }

    public void saveProfile(Long id, MultipartFile file) {
        Member found = getMember(id);

        String key = s3Service.upload(file);
        found.saveProfile(key);

        memberRepository.save(found);
    }

    public ProfileImageResponse getProfileUrl(Long id) {
        Member found = getMember(id);
        String key = found.getProfile();

        return new ProfileImageResponse(s3Service.download(key));
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 팀원을 찾을 수 없습니다."));
    }
}