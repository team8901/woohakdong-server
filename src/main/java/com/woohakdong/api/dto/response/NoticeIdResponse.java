package com.woohakdong.api.dto.response;

public record NoticeIdResponse(
        Long noticeId
) {
    public static NoticeIdResponse of(Long noticeId) {
        return new NoticeIdResponse(noticeId);
    }
}
