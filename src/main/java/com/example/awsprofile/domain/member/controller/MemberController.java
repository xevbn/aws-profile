package com.example.awsprofile.domain.member.controller;

import com.example.awsprofile.domain.common.annotation.LogExecution;
import com.example.awsprofile.domain.member.dto.request.MemberCreateRequest;
import com.example.awsprofile.domain.member.dto.response.MemberResponse;
import com.example.awsprofile.domain.member.dto.response.ProfileImageResponse;
import com.example.awsprofile.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/{id}/profile-image")
    public ResponseEntity<Void> saveProfileImage(@PathVariable long id, @RequestParam("file") MultipartFile file) {
        memberService.saveProfile(id, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageResponse> getProfileImage(@PathVariable long id) {
        ProfileImageResponse resBody = memberService.getProfileUrl(id);

        return ResponseEntity.ok(resBody);
    }
}
