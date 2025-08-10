package com.woohakdong.api.dto.response;

import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.club.model.FormAnswer;

import java.time.LocalDate;
import java.util.List;

public record ClubApplicationSubmissionResponse(
        Long clubApplicationSubmissionId,
        List<FormAnswerDto> formAnswers,
        LocalDate submittedAt
) {
    public static ClubApplicationSubmissionResponse of(ClubApplicationSubmissionEntity clubApplicationSubmission) {
        return new ClubApplicationSubmissionResponse(
                clubApplicationSubmission.getId(),
                clubApplicationSubmission.getFormAnswers().stream()
                        .map(FormAnswerDto::from)
                        .toList(),
                clubApplicationSubmission.getApplicatedAt()
        );
    }

    private record FormAnswerDto(
            Integer order,
            String question,
            Boolean required,
            Object answer
    ) {
        public static FormAnswerDto from(FormAnswer formAnswer) {
            return new FormAnswerDto(
                    formAnswer.order(),
                    formAnswer.question(),
                    formAnswer.required(),
                    formAnswer.answer()
            );
        }
    }
}
