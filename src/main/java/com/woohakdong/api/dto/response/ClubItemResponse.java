package com.woohakdong.api.dto.response;

import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import java.time.LocalDate;

public record ClubItemResponse(
        Long id,
        String name,
        String photo,
        String description,
        String location,
        ClubItemCategory category,
        Integer rentalMaxDay,
        Boolean available,
        Boolean using,
        LocalDate rentalDate,
        Integer rentalTime
) {
    public static ClubItemResponse from(ClubItemEntity entity) {
        return new ClubItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getPhoto(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getCategory(),
                entity.getRentalMaxDay(),
                entity.getAvailable(),
                entity.getUsing(),
                entity.getRentalDate(),
                entity.getRentalTime()
        );
    }
}
