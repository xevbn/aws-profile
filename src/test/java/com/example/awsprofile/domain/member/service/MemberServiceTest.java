package com.example.awsprofile.domain.member.service;

import com.example.awsprofile.domain.common.exception.NotFoundException;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
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
}
