package com.woohakdong.domain.clubitem.model;

public record ClubItemUpdateCommand(
        String name,
        String photo,
        String description,
        String location,
        ClubItemCategory category,
        Integer rentalMaxDay,
        Boolean available
) {
}
