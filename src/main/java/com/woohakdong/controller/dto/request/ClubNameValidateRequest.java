package com.woohakdong.controller.dto.request;

import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ClubNameValidateRequest(
        @Schema(example = "우학동", description = "동아리의 이름")
        @NotBlank(message = "동아리 이름은 필수입니다.")
        String name,

        @Schema(example = "woohakdong", description = "동아리의 영문 이름")
        @NotBlank(message = "동아리 영문 이름은 필수입니다.")
        String nameEn
) {
    public ClubNameValidateQuery toQueryModel() {
        return new ClubNameValidateQuery(name, nameEn);
    }
}
