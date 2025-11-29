package com.woohakdong.api.dto.response;

public record ClubItemIdResponse(
        Long clubItemId
) {
    public static ClubItemIdResponse of(Long clubItemId) {
        return new ClubItemIdResponse(clubItemId);
    }
}
