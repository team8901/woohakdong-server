package com.woohakdong.api.controller;

import com.woohakdong.api.dto.request.ScheduleCreateRequest;
import com.woohakdong.api.dto.request.ScheduleUpdateRequest;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.dto.response.ScheduleIdResponse;
import com.woohakdong.api.dto.response.ScheduleResponse;
import com.woohakdong.api.facade.ScheduleFacade;
import com.woohakdong.framework.security.RequestUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clubs/{clubId}/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "일정 관련 API")
public class ScheduleController {

    private final ScheduleFacade scheduleFacade;

    @Operation(summary = "일정 등록", description = "동아리 일정을 등록합니다. 회장만 가능합니다.")
    @PostMapping
    public ScheduleIdResponse createSchedule(@AuthenticationPrincipal RequestUser user,
                                             @PathVariable Long clubId,
                                             @Valid @RequestBody ScheduleCreateRequest request) {
        Long userAuthId = user.getUserAuthId();
        return scheduleFacade.createSchedule(clubId, userAuthId, request);
    }

    @Operation(summary = "일정 목록 조회", description = "동아리 일정 목록을 조회합니다. 시작 일시 오름차순 정렬.")
    @GetMapping
    public ListWrapper<ScheduleResponse> getSchedules(@PathVariable Long clubId) {
        return scheduleFacade.getSchedules(clubId);
    }

    @Operation(summary = "일정 단건 조회", description = "동아리 일정을 조회합니다.")
    @GetMapping("/{scheduleId}")
    public ScheduleResponse getSchedule(@PathVariable Long clubId,
                                        @PathVariable Long scheduleId) {
        return scheduleFacade.getSchedule(clubId, scheduleId);
    }

    @Operation(summary = "일정 수정", description = "동아리 일정을 수정합니다. 회장만 가능합니다.")
    @PutMapping("/{scheduleId}")
    public void updateSchedule(@AuthenticationPrincipal RequestUser user,
                               @PathVariable Long clubId,
                               @PathVariable Long scheduleId,
                               @Valid @RequestBody ScheduleUpdateRequest request) {
        Long userAuthId = user.getUserAuthId();
        scheduleFacade.updateSchedule(clubId, scheduleId, userAuthId, request);
    }

    @Operation(summary = "일정 삭제", description = "동아리 일정을 삭제합니다. 회장만 가능합니다.")
    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@AuthenticationPrincipal RequestUser user,
                               @PathVariable Long clubId,
                               @PathVariable Long scheduleId) {
        Long userAuthId = user.getUserAuthId();
        scheduleFacade.deleteSchedule(clubId, scheduleId, userAuthId);
    }
}
