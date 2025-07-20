package com.woohakdong.controller.dto.response;

public record ClubIdResponse(
        Long clubId
) {
    public static ClubIdResponse of(Long clubId) {
        return new ClubIdResponse(clubId);
    }
}
