package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.ClubItemRegisterRequest;
import com.woohakdong.api.dto.request.ClubItemRentRequest;
import com.woohakdong.api.dto.request.ClubItemReturnRequest;
import com.woohakdong.api.dto.request.ClubItemUpdateRequest;
import com.woohakdong.api.dto.response.ClubItemHistoryResponse;
import com.woohakdong.api.dto.response.ClubItemIdResponse;
import com.woohakdong.api.dto.response.ClubItemRentResponse;
import com.woohakdong.api.dto.response.ClubItemResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ClubItemFacade;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "동아리 물품 등록", description = "특정 동아리에 새로운 물품을 등록합니다.")
    @PostMapping
    public ClubItemIdResponse addClubItem(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @Valid @RequestBody ClubItemRegisterRequest request
    ) {
        return clubItemFacade.addClubItem(clubId, request.toCommand());
    }

    @Operation(summary = "동아리 물품 수정", description = "특정 동아리의 물품 정보를 수정합니다.")
    @PutMapping("/{itemId}")
    public void updateClubItem(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @PathVariable Long itemId,
            @Valid @RequestBody ClubItemUpdateRequest request
    ) {
        clubItemFacade.updateClubItem(clubId, itemId, request.toCommand());
    }

    @Operation(summary = "동아리 물품 삭제", description = "특정 동아리의 물품을 삭제합니다.")
    @DeleteMapping("/{itemId}")
    public void deleteClubItem(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @PathVariable Long itemId
    ) {
        clubItemFacade.deleteClubItem(clubId, itemId);
    }

    @Operation(summary = "동아리 물품 대여", description = "특정 동아리의 물품을 대여합니다.")
    @PostMapping("/{itemId}/rent")
    public ClubItemRentResponse rentClubItem(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @PathVariable Long itemId,
            @Valid @RequestBody ClubItemRentRequest request
    ) {
        return clubItemFacade.rentClubItem(clubId, itemId, user.getUserAuthId(), request.rentalDays());
    }

    @Operation(summary = "동아리 물품 반납", description = "대여한 물품을 반납합니다.")
    @PostMapping("/{itemId}/return")
    public void returnClubItem(
            @AuthenticationPrincipal RequestUser user,
            @PathVariable Long clubId,
            @PathVariable Long itemId,
            @RequestBody(required = false) ClubItemReturnRequest request
    ) {
        String returnImage = request != null ? request.returnImage() : null;
        clubItemFacade.returnClubItem(clubId, itemId, returnImage);
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
