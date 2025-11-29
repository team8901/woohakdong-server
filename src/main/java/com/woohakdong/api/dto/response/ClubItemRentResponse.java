package com.woohakdong.api.dto.response;

import java.time.LocalDate;

public record ClubItemRentResponse(
        Long historyId,
        LocalDate rentalDate,
        LocalDate dueDate
) {
    public static ClubItemRentResponse of(Long historyId, LocalDate rentalDate, LocalDate dueDate) {
        return new ClubItemRentResponse(historyId, rentalDate, dueDate);
    }
}
