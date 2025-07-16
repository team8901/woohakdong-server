package com.woohakdong.controller.dto.request;

import com.woohakdong.domain.club.model.ClubRegisterCommand;

public record ClubRegisterRequest (
        String name,
        String nameEn,
        String description,
        String thumbnailImageUrl,
        String bannerImageUrl,
        String roomInfo,
        String groupChatLink,
        String groupChatPassword,
        Integer dues
){

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
