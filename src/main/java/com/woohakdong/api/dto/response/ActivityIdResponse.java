package com.woohakdong.api.dto.response;

public record ActivityIdResponse(
        Long activityId
) {
    public static ActivityIdResponse of(Long activityId) {
        return new ActivityIdResponse(activityId);
    }
}
