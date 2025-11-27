package com.woohakdong.api.dto.response;

import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import java.time.LocalDate;

public record ClubItemHistoryResponse(
        Long id,
        Long itemId,
        Long memberId,
        String name,
        String memberName,
        ClubItemCategory category,
        LocalDate rentalDate,
        LocalDate dueDate,
        LocalDate returnDate,
        String returnImage,
        Boolean overdue
) {
    public static ClubItemHistoryResponse from(ClubItemHistoryEntity entity) {
        return new ClubItemHistoryResponse(
                entity.getId(),
                entity.getClubItem().getId(),
                entity.getClubMembership().getId(),
                entity.getClubItem().getName(),
                entity.getClubMembership().getNickname(),
                entity.getClubItem().getCategory(),
                entity.getRentalDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                entity.getReturnImage(),
                entity.isOverdue()
        );
    }
}
