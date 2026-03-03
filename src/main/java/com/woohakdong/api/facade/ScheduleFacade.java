package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.ScheduleCreateRequest;
import com.woohakdong.api.dto.request.ScheduleUpdateRequest;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.dto.response.ScheduleIdResponse;
import com.woohakdong.api.dto.response.ScheduleResponse;
import com.woohakdong.domain.schedule.application.ScheduleService;
import com.woohakdong.domain.schedule.model.ScheduleEntity;
import com.woohakdong.domain.user.application.UserService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {

    private final ScheduleService scheduleService;
    private final UserService userService;

    public ScheduleIdResponse createSchedule(Long clubId, Long userAuthId, ScheduleCreateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        Long scheduleId = scheduleService.createSchedule(clubId, userProfile, request.toCommand());
        return ScheduleIdResponse.of(scheduleId);
    }

    public ListWrapper<ScheduleResponse> getSchedules(Long clubId) {
        List<ScheduleEntity> schedules = scheduleService.getSchedules(clubId);
        return ListWrapper.of(schedules.stream()
                .map(ScheduleResponse::of)
                .toList());
    }

    public ScheduleResponse getSchedule(Long clubId, Long scheduleId) {
        ScheduleEntity schedule = scheduleService.getSchedule(clubId, scheduleId);
        return ScheduleResponse.of(schedule);
    }

    public void updateSchedule(Long clubId, Long scheduleId, Long userAuthId, ScheduleUpdateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        scheduleService.updateSchedule(clubId, scheduleId, userProfile, request.toCommand());
    }

    public void deleteSchedule(Long clubId, Long scheduleId, Long userAuthId) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        scheduleService.deleteSchedule(clubId, scheduleId, userProfile);
    }
}
