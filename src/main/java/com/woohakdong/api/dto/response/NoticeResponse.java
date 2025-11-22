package com.woohakdong.api.dto.response;

import com.woohakdong.domain.notice.model.NoticeEntity;

import java.time.LocalDate;

public record NoticeResponse(
        Long id,
        Boolean isPinned,
        String title,
        LocalDate updatedAt,
        String writer,
        String content
) {
    public static NoticeResponse of(NoticeEntity notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getIsPinned(),
                notice.getTitle(),
                notice.getUpdatedAt(),
                notice.getWriter().getNickname(),
                notice.getContent()
        );
    }
}
