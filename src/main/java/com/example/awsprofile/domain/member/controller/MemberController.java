package com.example.awsprofile.domain.member.controller;

import com.example.awsprofile.domain.common.annotation.LogExecution;
import com.example.awsprofile.domain.member.dto.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.MemberResponse;
import com.example.awsprofile.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@LogExecution
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberCreateRequest createRequest) {
        memberService.createMember(createRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getAllMembers(@PathVariable long id) {
        MemberResponse res =  memberService.findMember(id);

        return ResponseEntity.ok(res);
    }
}
