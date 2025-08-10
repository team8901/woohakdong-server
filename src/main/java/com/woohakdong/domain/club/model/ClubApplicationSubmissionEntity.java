package com.woohakdong.domain.club.model;

import com.woohakdong.domain.club.infrastructure.storage.FormAnswerListConverter;
import com.woohakdong.domain.user.model.UserProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "club_application_submission")
public class ClubApplicationSubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_application_form_id", nullable = false)
    private ClubApplicationFormEntity clubApplicationForm;

    @Convert(converter = FormAnswerListConverter.class)
    @Column(name = "form_answers", columnDefinition = "TEXT")
    private List<FormAnswer> formAnswers;

    @JoinColumn(name = "user_profile_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private UserProfileEntity userProfile;

    @Column(name = "applicated_at", updatable = false)
    private LocalDate applicatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ClubApplicationStatus applicationStatus;

    public static ClubApplicationSubmissionEntity create(ClubApplicationSubmissionCommand command,
                                                         ClubApplicationFormEntity clubApplicationForm,
                                                         UserProfileEntity userProfile) {
        return new ClubApplicationSubmissionEntity(
                null,
                clubApplicationForm,
                command.formAnswers(),
                userProfile,
                LocalDate.now(),
                ClubApplicationStatus.PENDING
        );
    }

}
