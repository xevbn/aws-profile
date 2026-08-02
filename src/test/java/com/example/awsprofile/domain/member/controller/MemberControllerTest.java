package com.example.awsprofile.domain.member.controller;

import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
import com.example.awsprofile.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MemberService memberService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("멤버 생성 엔드포인트 테스트 - 성공")
    void api_members_POST() throws Exception {
        //given
        MemberCreateRequest createRequest = new MemberCreateRequest(
                "name",
                20,
                "intp"
        );

        //when&then
        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("멤버 정보 조회 엔드포인트 테스트 - 성공")
    void api_members_GET() throws Exception {
        //given
        MemberResponse res = new MemberResponse(
                1L,
                "name",
                20,
                "intp",
                "profile-url"
        );
        given(memberService.findMember(anyLong())).willReturn(res);


        //when&then
        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(res.name()));
    }

}
