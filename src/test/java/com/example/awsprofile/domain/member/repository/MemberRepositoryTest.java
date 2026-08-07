package com.example.awsprofile.domain.member.repository;

import com.example.awsprofile.domain.member.MemberFixture;
import com.example.awsprofile.domain.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import com.example.awsprofile.domain.member.support.MySQLTestContainerConfig;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MySQLTestContainerConfig.class)
@ActiveProfiles("test")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository repo;

    @Test
    @DisplayName("member 저장 테스트 - 성공")
    void saveTest_success() {
        //given
        Member member = MemberFixture.create(
                "name",
                20,
                "intp"
        );

        //when
        Member saved = repo.save(member);

        //then
        assertEquals(saved.getId(), repo.findById(saved.getId()).get().getId());
    }

    @Test
    @DisplayName("단일 조회 테스트 - 조회값이 존재함")
    void findByIdTest_success() {
        //given
        Member member = MemberFixture.create(
                "name",
                20,
                "intp"
        );
        Member saved = repo.save(member);

        //when
        Member found = repo.findById(saved.getId()).orElse(null);

        //then
        assertNotNull(found);
        assertEquals(member.getName(), found.getName());
    }

    @Test
    @DisplayName("조회 테스트 - 조회값이 존재하지 않음")
    void findByIdTest_NotFound() {
        //when&then
        assertTrue(repo.findById(1L).isEmpty());
    }

    @Test
    @DisplayName("삭제 테스트 - 성공")
    void deleteTest_success() {
        //given
        Member member = MemberFixture.create(
                "name",
                20,
                "intp"
        );

        Member saved = repo.save(member);

        //when
        repo.deleteById(saved.getId());

        //then
        assertTrue(repo.findById(saved.getId()).isEmpty());
    }
}
