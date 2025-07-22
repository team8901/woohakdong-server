package com.woohakdong.controller.dto.request;

import com.woohakdong.domain.club.model.ClubRegisterCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClubRegisterRequest(

        @Schema(example = "우학동", description = "동아리의 이름")
        @NotBlank(message = "동아리 이름은 필수입니다.")
        String name,

        @Schema(example = "woohakdong", description = "동아리의 영문 이름")
        @NotBlank(message = "동아리 영문 이름은 필수입니다.")
        String nameEn,

        @Schema(example = "우학동은 우리 학교 동아리의 줄임말입니다.", description = "동아리의 설명")
        String description,

        @Schema(example = "https://example.com/thumbnail.jpg", description = "동아리 썸네일 이미지 URL")
        String thumbnailImageUrl,

        @Schema(example = "https://example.com/banner.jpg", description = "동아리 배너 이미지 URL")
        String bannerImageUrl,

        @Schema(example = "팔달관 334호", description = "동아리 방 정보")
        String roomInfo,

        @Schema(example = "https://example.com/group-chat", description = "동아리 단체 채팅방 링크")
        String groupChatLink,

        @Schema(example = "password123", description = "동아리 단체 채팅방 비밀번호")
        String groupChatPassword,

        @Schema(example = "10000", description = "동아리 회비")
        @Min(value = 0, message = "동아리 회비는 0원 이상이어야 합니다.")
        Integer dues
) {

    public ClubRegisterCommand toCommand() {
        return new ClubRegisterCommand(
                name,
                nameEn,
                description,
                thumbnailImageUrl,
                bannerImageUrl,
                roomInfo,
                groupChatLink,
                groupChatPassword,
                dues
        );
    }
}
