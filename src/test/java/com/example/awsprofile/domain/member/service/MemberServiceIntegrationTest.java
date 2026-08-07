package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.member.MemberFixture;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.entity.Member;
import com.example.awsprofile.domain.member.repository.MemberRepository;
import com.example.awsprofile.domain.member.support.MySQLTestContainerConfig;
import com.example.awsprofile.domain.member.support.S3TestSupport;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(MySQLTestContainerConfig.class)
public class MemberServiceIntegrationTest extends S3TestSupport {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private S3Template s3Template;

    @BeforeEach
    void setup() {
        if(!s3Template.bucketExists("test")) {
            s3Template.createBucket("test");
        }
    }

    @Test
    @DisplayName("멤버 생성 통합 테스트 - 성공")
    void createMemberTest_success() throws Exception {
        //given
        MemberCreateRequest createRequest = new MemberCreateRequest(
                "name",
                20,
                "asdf"
        );

        //when
        memberService.createMember(createRequest);

        //then
        assertNotNull(memberRepository.findById(1L));
    }

    @Test
    @DisplayName("프로필 이미지 업로드 테스트 - 성공")
    void updateProfileTest_success() throws Exception {
        //given
        Member test = MemberFixture.create("name", 20, "asdf");
        Member saved = memberRepository.save(test);
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);

        //when
        memberService.saveOrUpdateProfile(saved.getId(), mockMultipartFile);

        //then
        assertNotNull(memberRepository.findById(saved.getId()).map(Member::getProfile).orElse(null));
    }

    @Test
    @DisplayName("멤버 삭제 테스트 - 성공")
    void deleteProfileTest_success() throws Exception {
        //given
        Member test = MemberFixture.create("name", 20, "asdf");
        Member saved = memberRepository.save(test);
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        memberService.saveOrUpdateProfile(saved.getId(), mockMultipartFile);
        String key = saved.getProfile();

        //when
        memberService.deleteMember(saved.getId());

        //then
        assertTrue(memberRepository.findById(saved.getId()).isEmpty());
    }
}
