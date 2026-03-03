package com.woohakdong.api.dto.response;

import com.woohakdong.domain.schedule.model.ScheduleEntity;

import java.time.LocalDateTime;

public record ScheduleResponse(
        Long id,
        String title,
        String content,
        String color,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String writer
) {
    public static ScheduleResponse of(ScheduleEntity schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getColor(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getWriter().getNickname()
        );
    }
}
