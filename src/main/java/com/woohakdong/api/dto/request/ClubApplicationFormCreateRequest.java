package com.woohakdong.api.dto.request;

import com.woohakdong.domain.club.model.ClubApplicationFormCreateCommand;
import com.woohakdong.domain.club.model.FormQuestion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ClubApplicationFormCreateRequest (
        @Schema(example = "2025-1기 신입생 모집", description = "동아리 신청서 제목")
        @NotBlank(message = "동아리 신청서 제목은 필수입니다.")
        String name,

        @Schema(example = "[{\"question\": \"이름은 무엇인가요?\", \"type\": \"TEXT\", \"required\": true}," +
                " {\"question\": \"취미는 무엇인가요?\", \"type\": \"SELECT\", \"options\": [\"독서\", \"운동\", \"여행\"]," +
                " \"required\": false}]", description = "동아리 신청서 내용")
        List<FormQuestion> formContent
){
    public ClubApplicationFormCreateCommand toCommandModel() {
        return new ClubApplicationFormCreateCommand(name, formContent);
    }
}
