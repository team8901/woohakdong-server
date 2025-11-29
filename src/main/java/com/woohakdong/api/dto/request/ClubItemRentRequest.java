package com.woohakdong.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ClubItemRentRequest(

        @Schema(example = "3", description = "대여 일수 (물품별 최대 대여 일수 이내)")
        @Min(value = 1, message = "대여 일수는 1일 이상이어야 합니다.")
        @Max(value = 365, message = "대여 일수는 365일 이하여야 합니다.")
        Integer rentalDays
) {
}
