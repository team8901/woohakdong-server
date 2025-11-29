package com.woohakdong.api.dto.response;

import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import java.time.LocalDate;

public record ClubItemDetailResponse(
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
        Integer rentalTime,
        // 대여자 정보 (using=true일 때만 값 있음)
        Long borrowerId,
        String borrowerName,
        LocalDate dueDate
) {
    public static ClubItemDetailResponse from(ClubItemEntity entity, ClubItemHistoryEntity activeRental) {
        Long borrowerId = null;
        String borrowerName = null;
        LocalDate dueDate = null;

        if (activeRental != null) {
            borrowerId = activeRental.getClubMembership().getId();
            borrowerName = activeRental.getClubMembership().getNickname();
            dueDate = activeRental.getDueDate();
        }

        return new ClubItemDetailResponse(
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
                entity.getRentalTime(),
                borrowerId,
                borrowerName,
                dueDate
        );
    }
}
