package com.woohakdong.domain.activity.model;

import java.time.LocalDate;
import java.util.List;

public record ActivityCreateCommand(
        String title,
        String content,
        Integer participantCount,
        LocalDate activityDate,
        ActivityTag tag,
        List<String> activityImages
) {
}
