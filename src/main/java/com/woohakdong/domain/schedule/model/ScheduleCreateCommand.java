package com.woohakdong.domain.schedule.model;

import java.time.LocalDateTime;

public record ScheduleCreateCommand(
        String title,
        String content,
        String color,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
