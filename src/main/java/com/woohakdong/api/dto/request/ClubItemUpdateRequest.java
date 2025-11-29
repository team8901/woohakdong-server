package com.woohakdong.api.dto.request;

import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClubItemUpdateRequest(

        @Schema(example = "캠핑 텐트", description = "물품 이름")
        @NotBlank(message = "물품 이름은 필수입니다.")
        String name,

        @Schema(example = "https://example.com/photo.jpg", description = "물품 사진 URL")
        String photo,

        @Schema(example = "4인용 캠핑 텐트입니다.", description = "물품 설명")
        String description,

        @Schema(example = "동아리방 캐비닛 3번", description = "물품 보관 위치")
        String location,

        @Schema(example = "SPORT", description = "물품 카테고리")
        @NotNull(message = "물품 카테고리는 필수입니다.")
        ClubItemCategory category,

        @Schema(example = "7", description = "최대 대여 가능 일수")
        @NotNull(message = "최대 대여 일수는 필수입니다.")
        @Min(value = 1, message = "최대 대여 일수는 1일 이상이어야 합니다.")
        Integer rentalMaxDay,

        @Schema(example = "true", description = "대여 가능 여부")
        @NotNull(message = "대여 가능 여부는 필수입니다.")
        Boolean available
) {

    public ClubItemUpdateCommand toCommand() {
        return new ClubItemUpdateCommand(
                name,
                photo,
                description,
                location,
                category,
                rentalMaxDay,
                available
        );
    }
}
