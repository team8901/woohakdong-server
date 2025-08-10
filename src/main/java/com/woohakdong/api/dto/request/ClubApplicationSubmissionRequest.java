package com.woohakdong.api.dto.request;

import com.woohakdong.domain.club.model.ClubApplicationSubmissionCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Map;

public record ClubApplicationSubmissionRequest(
        @Schema(example =
                "[{\"order\": 1, \"question\": \"이름은 무엇인가요?\", \"answer\": \"홍길동\", \"required\": true}," +
                        "{\"order\": 2, \"question\": \"취미는 무엇인가요?\", \"answer\": [\"독서\", \"운동\"], \"required\": false}]",
                description = "동아리 신청서에 대한 답변 리스트")
        @NotEmpty List<Map<String, Object>> answers
        // 각 질문에 대한 답변 리스트, 각 답변은 Map 형태로 { "question": "질문 내용", "answer": "답변 내용" } 형식
) {
    public ClubApplicationSubmissionCommand toCommandModel() {
        return ClubApplicationSubmissionCommand.from(answers);
    }
}
