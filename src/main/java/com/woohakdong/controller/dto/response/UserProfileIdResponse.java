package com.woohakdong.controller.dto.response;

public record UserProfileIdResponse(
        Long userProfileId
) {
    public static UserProfileIdResponse from(Long userProfileId) {
        return new UserProfileIdResponse(userProfileId);
    }
}
