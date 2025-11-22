package com.woohakdong.domain.notice.model;

public record NoticeCreateCommand(
        String title,
        String content,
        Boolean isPinned
) {
}
