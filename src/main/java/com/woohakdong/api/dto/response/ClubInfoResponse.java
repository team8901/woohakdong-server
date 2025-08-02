package com.woohakdong.api.dto.response;

import com.woohakdong.domain.club.model.ClubEntity;

public record ClubInfoResponse(
        Long id,
        String name,
        String nameEn,
        String description,
        String thumbnailImageUrl,
        String bannerImageUrl,
        String roomInfo,
        String groupChatLink,
        Integer dues
) {
    public static ClubInfoResponse of(ClubEntity clubEntity) {
        return new ClubInfoResponse(
                clubEntity.getId(),
                clubEntity.getName(),
                clubEntity.getNameEn(),
                clubEntity.getDescription(),
                clubEntity.getThumbnailImageUrl(),
                clubEntity.getBannerImageUrl(),
                clubEntity.getRoomInfo(),
                clubEntity.getGroupChatLink(),
                clubEntity.getDues()
        );
    }
}
