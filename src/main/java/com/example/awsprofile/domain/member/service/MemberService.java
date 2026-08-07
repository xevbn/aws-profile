package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.MemberDeleteFailureException;
import com.example.awsprofile.domain.common.exception.NotFoundException;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
import com.example.awsprofile.domain.member.dto.response.ProfileImageResponse;
import com.example.awsprofile.domain.member.entity.Member;
import com.example.awsprofile.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    @Value("${cloudfront.domain}")
    private String domain;

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

    public void saveOrUpdateProfile(Long id, MultipartFile file) {
        Member found = getMember(id);

        if(found.getProfile() == null) {
            String key = s3Service.upload(file);
            found.saveProfile(key);
        } else {
            String oldKey = found.getProfile();
            s3Service.delete(oldKey);

            String newKey = s3Service.upload(file);
            found.saveProfile(newKey);
        }

        memberRepository.save(found);
    }

    public ProfileImageResponse getProfileUrl(Long id) {
        Member found = getMember(id);
        String key = found.getProfile();

        if(key == null) {
            throw new NotFoundException("프로필 사진이 없습니다.");
        }

        String url = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(domain)
                .path(key)
                .toUriString();

        return new ProfileImageResponse(url);
    }

    public void deleteMember(Long id) {
        Member toDelete = getMember(id);

        try {
            s3Service.delete(toDelete.getProfile());
        } catch (Exception ex) {
            throw new MemberDeleteFailureException("멤버 삭제 중 오류가 발생했습니다.");
        }

        memberRepository.delete(toDelete);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 id의 팀원을 찾을 수 없습니다."));
    }
}