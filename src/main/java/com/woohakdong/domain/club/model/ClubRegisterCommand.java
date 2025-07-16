package com.woohakdong.domain.club.model;

public record ClubRegisterCommand(
        String name,
        String nameEn,
        String description,
        String thumbnailImageUrl,
        String bannerImageUrl,
        String roomInfo,
        String groupChatLink,
        String groupChatPassword,
        Integer dues
) {
}
