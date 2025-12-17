package com.woohakdong.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubApplicationRejectRequest(

        @Schema(example = "필수 문항 미작성으로 인한 거절", description = "거절 사유 (선택)")
        String rejectionReason
) {
}
