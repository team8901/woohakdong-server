package com.woohakdong.domain.clubitem.model;

public record ClubItemRegisterCommand(
        String name,
        String photo,
        String description,
        String location,
        ClubItemCategory category,
        Integer rentalMaxDay
) {
}
