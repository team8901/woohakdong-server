package com.woohakdong.api.dto.response;

public record ScheduleIdResponse(
        Long scheduleId
) {
    public static ScheduleIdResponse of(Long scheduleId) {
        return new ScheduleIdResponse(scheduleId);
    }
}
