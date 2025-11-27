package com.woohakdong.api.controller;

import com.woohakdong.api.dto.response.ClubItemHistoryResponse;
import com.woohakdong.api.dto.response.ClubItemResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ClubItemFacade;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clubs/{clubId}/items")
@RequiredArgsConstructor
@Tag(name = "Club Item", description = "동아리 물품 관련 API")
public class ClubItemController {

    private final ClubItemFacade clubItemFacade;

    @Operation(summary = "동아리 물품 목록 조회", description = "특정 동아리의 물품 목록을 조회합니다.")
    @GetMapping
    public ListWrapper<ClubItemResponse> getClubItems(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ClubItemCategory category
    ) {
        return clubItemFacade.getClubItems(clubId, keyword, category);
    }

    @Operation(summary = "동아리 물품 대여 내역 조회", description = "특정 동아리의 물품 대여 내역을 조회합니다.")
    @GetMapping("/history")
    public ListWrapper<ClubItemHistoryResponse> getClubItemHistory(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId
    ) {
        return clubItemFacade.getClubItemHistory(clubId);
    }
}
