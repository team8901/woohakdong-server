package com.woohakdong.domain.notice.model;

public record NoticeUpdateCommand(
        String title,
        String content,
        Boolean isPinned
) {
}
