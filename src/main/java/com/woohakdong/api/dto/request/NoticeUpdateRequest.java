package com.woohakdong.api.dto.request;

import com.woohakdong.domain.notice.model.NoticeUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoticeUpdateRequest(

        @Schema(example = "수정된 공지사항 제목", description = "공지사항 제목")
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @Schema(example = "수정된 공지사항 내용입니다.", description = "공지사항 내용")
        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @Schema(example = "false", description = "고정 여부")
        @NotNull(message = "고정 여부는 필수입니다.")
        Boolean isPinned
) {
    public NoticeUpdateCommand toCommand() {
        return new NoticeUpdateCommand(title, content, isPinned);
    }
}
