package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.FileUploadException;
import com.example.awsprofile.domain.common.exception.NotFoundException;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
import com.example.awsprofile.domain.member.dto.response.ProfileImageResponse;
import com.example.awsprofile.domain.member.entity.Member;
import com.example.awsprofile.domain.member.repository.MemberRepository;
import com.example.awsprofile.domain.member.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private MemberService service;

    private Member member;

    @BeforeEach
    void setUp() {
        member = MemberFixture.create(
                "name",
                20,
                "intp"
        );
    }

    @Test
    @DisplayName("멤버 정보를 저장한다")
    void createMember_success() {
        //given
        MemberCreateRequest createRequest = new MemberCreateRequest(
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        service.createMember(createRequest);

        //then
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("해당 id의 멤버 정보를 조회한다.")
    void findMemberById_success() {
        //given
        ReflectionTestUtils.setField(member, "id", 1L);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        MemberResponse res = service.findMember(1L);

        //then
        assertEquals("name", res.name());
    }

    @Test
    @DisplayName("해당 id의 멤버가 없을 시 NotFoundException을 던진다.")
    void findMemberById_fail_notFound() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        //when&then
        assertThrows(NotFoundException.class, () -> service.findMember(1L));
    }

    @Test
    @DisplayName("해당 멤버에 프로필을 저장한다 - s3 key를 저장한다.")
    void saveProfile_success() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(s3Service.upload(any())).willReturn("s3key");

        //when
        service.saveOrUpdateProfile(1L, mockMultipartFile);

        //then
        verify(memberRepository).save(any(Member.class));
        assertEquals("s3key", member.getProfile());
    }

    @Test
    @DisplayName("해당 멤버의 프로필을 변경한다 - s3 key를 수정한다")
    void updateProfile_success() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", inputStream);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(s3Service.upload(any())).willReturn("newS3key");

        //when
        service.saveOrUpdateProfile(1L, mockMultipartFile);

        //then
        verify(memberRepository).save(any(Member.class));
        assertEquals("newS3key", member.getProfile());
    }

    @Test
    @DisplayName("파일 업로드 중 예외 발생")
    void saveProfile_fail() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(s3Service.upload(any())).willThrow(new FileUploadException("S3 파일 업로드 중 오류가 발생했습니다. 파일명: file"));

        //when&then
        assertThrows(FileUploadException.class, () -> service.saveOrUpdateProfile(1L, mock(MultipartFile.class)));
    }

    @Test
    @DisplayName("해당 id의 멤버의 profile url을 얻어온다.")
    void getProfileUrl_success() {
        //given
        String domain = "cloudfront.net";
        ReflectionTestUtils.setField(service, "domain", domain);
        member.saveProfile("key");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        ProfileImageResponse res = service.getProfileUrl(1L);

        //then
        assertEquals("https://cloudfront.net/key", res.profileImageURL());
    }

    @Test
    @DisplayName("해당 id의 멤버를 삭제한다")
    void deleteMember_success() {
        //given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        service.deleteMember(1L);

        //then
        verify(s3Service).delete(any());
        verify(memberRepository).delete(any(Member.class));
    }
}
