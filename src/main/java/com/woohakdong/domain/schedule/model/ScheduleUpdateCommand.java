package com.woohakdong.domain.schedule.model;

import java.time.LocalDateTime;

public record ScheduleUpdateCommand(
        String title,
        String content,
        String color,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
