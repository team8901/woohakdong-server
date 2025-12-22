package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.ActivityCreateRequest;
import com.woohakdong.api.dto.request.ActivityUpdateRequest;
import com.woohakdong.api.dto.response.ActivityIdResponse;
import com.woohakdong.api.dto.response.ActivityResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ActivityFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs/{clubId}/activities")
@RequiredArgsConstructor
@Tag(name = "Activity", description = "활동 기록 관련 API")
public class ActivityController {

    private final ActivityFacade activityFacade;

    @Operation(summary = "활동 기록 작성", description = "동아리 활동 기록을 작성합니다.")
    @PostMapping
    public ActivityIdResponse createActivity(@AuthenticationPrincipal RequestUser user,
                                             @PathVariable Long clubId,
                                             @Valid @RequestBody ActivityCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return activityFacade.createActivity(clubId, userAuthId, request);
    }

    @Operation(summary = "활동 기록 목록 조회", description = "동아리 활동 기록 목록을 조회합니다. 활동 날짜 기준 최신순 정렬.")
    @GetMapping
    public ListWrapper<ActivityResponse> getActivities(@PathVariable Long clubId) {
        return activityFacade.getActivities(clubId);
    }

    @Operation(summary = "활동 기록 단건 조회", description = "동아리 활동 기록을 조회합니다.")
    @GetMapping("/{activityId}")
    public ActivityResponse getActivity(@PathVariable Long clubId,
                                        @PathVariable Long activityId) {
        return activityFacade.getActivity(clubId, activityId);
    }

    @Operation(summary = "활동 기록 수정", description = "동아리 활동 기록을 수정합니다.")
    @PutMapping("/{activityId}")
    public void updateActivity(@AuthenticationPrincipal RequestUser user,
                               @PathVariable Long clubId,
                               @PathVariable Long activityId,
                               @Valid @RequestBody ActivityUpdateRequest request) {
        Long userAuthId = user.getUserAuthId();
        activityFacade.updateActivity(clubId, activityId, userAuthId, request);
    }

    @Operation(summary = "활동 기록 삭제", description = "동아리 활동 기록을 삭제합니다.")
    @DeleteMapping("/{activityId}")
    public void deleteActivity(@AuthenticationPrincipal RequestUser user,
                               @PathVariable Long clubId,
                               @PathVariable Long activityId) {
        Long userAuthId = user.getUserAuthId();
        activityFacade.deleteActivity(clubId, activityId, userAuthId);
    }
}
