package com.woohakdong.api.dto.response;

import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.FormQuestion;

import java.time.LocalDate;
import java.util.List;

public record ClubApplicationFormInfoResponse(
        Long clubApplicationFormId,
        String name,
        List<FormQuestion> formContent,
        LocalDate createdAt
) {
    public static ClubApplicationFormInfoResponse of(ClubApplicationFormEntity clubApplicationFormEntity) {
        return new ClubApplicationFormInfoResponse(
                clubApplicationFormEntity.getId(),
                clubApplicationFormEntity.getName(),
                clubApplicationFormEntity.getFormContent(),
                clubApplicationFormEntity.getCreatedAt()
        );
    }
}
