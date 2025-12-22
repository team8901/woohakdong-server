package com.woohakdong.api.dto.response;

import com.woohakdong.domain.activity.model.ActivityEntity;
import com.woohakdong.domain.activity.model.ActivityTag;

import java.time.LocalDate;
import java.util.List;

public record ActivityResponse(
        Long id,
        String title,
        String content,
        String writer,
        LocalDate createdAt,
        LocalDate updatedAt,
        Integer participantCount,
        LocalDate activityDate,
        ActivityTag tag,
        List<String> activityImages
) {
    public static ActivityResponse of(ActivityEntity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getContent(),
                activity.getWriter().getNickname(),
                activity.getCreatedAt(),
                activity.getUpdatedAt(),
                activity.getParticipantCount(),
                activity.getActivityDate(),
                activity.getTag(),
                activity.getActivityImages()
        );
    }
}
