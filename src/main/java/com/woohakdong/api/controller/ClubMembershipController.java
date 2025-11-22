package com.woohakdong.api.controller;

import com.woohakdong.api.dto.response.ClubMembershipResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ClubMembershipFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}/members")
@RequiredArgsConstructor
@Tag(name = "Club Membership", description = "동아리 회원 관련 API")
public class ClubMembershipController {

    private final ClubMembershipFacade clubMembershipFacade;

    @Operation(summary = "동아리 회원 목록 조회", description = "특정 동아리의 회원 목록을 조회합니다.")
    @GetMapping
    public ListWrapper<ClubMembershipResponse> getClubMembers(@PathVariable Long clubId) {
        return clubMembershipFacade.getClubMembers(clubId);
    }
}
