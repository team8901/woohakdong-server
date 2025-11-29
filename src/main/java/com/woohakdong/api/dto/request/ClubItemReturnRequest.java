package com.woohakdong.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClubItemReturnRequest(

        @Schema(example = "https://example.com/return-image.jpg", description = "반납 인증 이미지 URL (선택)")
        String returnImage
) {
}
