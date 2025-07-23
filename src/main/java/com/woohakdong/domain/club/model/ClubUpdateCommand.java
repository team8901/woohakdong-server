package com.woohakdong.domain.club.model;

public record ClubUpdateCommand(
        String description,
        String thumbnailImageUrl,
        String bannerImageUrl,
        String roomInfo,
        String groupChatLink,
        String groupChatPassword,
        Integer dues
) {
}
