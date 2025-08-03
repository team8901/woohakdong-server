package com.woohakdong.api.dto.response;

public record ClubApplicationFormIdResponse(
        Long clubApplicationFormId
) {
    public static ClubApplicationFormIdResponse of(Long clubApplicationFormId) {
        return new ClubApplicationFormIdResponse(clubApplicationFormId);
    }
}
