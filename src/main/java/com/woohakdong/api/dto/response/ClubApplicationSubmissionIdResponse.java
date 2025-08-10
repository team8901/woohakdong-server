package com.woohakdong.api.dto.response;

public record ClubApplicationSubmissionIdResponse(
        Long clubApplicationFormSubmissionId
) {
    public static ClubApplicationSubmissionIdResponse of(Long submissionId) {
        return new ClubApplicationSubmissionIdResponse(submissionId);
    }
}
