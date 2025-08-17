package com.woohakdong.api.dto.response;

import com.woohakdong.domain.club.model.ClubApplicationStatus;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.club.model.FormAnswer;
import com.woohakdong.domain.user.model.UserProfileEntity;

import java.time.LocalDate;
import java.util.List;

public record ClubApplicationSubmissionResponse(
        Long clubApplicationSubmissionId,
        UserInfoDto user,
        List<FormAnswerDto> formAnswers,
        LocalDate submittedAt,
        ClubApplicationStatus applicationStatus
) {
    public static ClubApplicationSubmissionResponse of(ClubApplicationSubmissionEntity clubApplicationSubmission) {
        return new ClubApplicationSubmissionResponse(
                clubApplicationSubmission.getId(),
                UserInfoDto.of(clubApplicationSubmission.getUserProfile()),
                clubApplicationSubmission.getFormAnswers().stream()
                        .map(FormAnswerDto::from)
                        .toList(),
                clubApplicationSubmission.getApplicatedAt(),
                clubApplicationSubmission.getApplicationStatus()
        );
    }

    private record UserInfoDto(
            Long userProfileId,
            String name,
            String studentId,
            String phoneNumber,
            String email
    ) {
        public static UserInfoDto of(UserProfileEntity userProfile) {
            return new UserInfoDto(
                    userProfile.getId(),
                    userProfile.getName(),
                    userProfile.getStudentId(),
                    userProfile.getPhoneNumber(),
                    userProfile.getEmail()
            );
        }

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
