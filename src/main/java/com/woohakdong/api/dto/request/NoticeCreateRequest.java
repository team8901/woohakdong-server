package com.woohakdong.api.dto.request;

import com.woohakdong.domain.notice.model.NoticeCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record NoticeCreateRequest(

        @Schema(example = "공지사항 제목", description = "공지사항 제목")
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @Schema(example = "공지사항 내용입니다.", description = "공지사항 내용")
        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @Schema(example = "false", description = "공지사항 고정 여부")
        Boolean isPinned
) {
    public NoticeCreateCommand toCommand() {
        return new NoticeCreateCommand(title, content, isPinned != null ? isPinned : false);
    }
}
