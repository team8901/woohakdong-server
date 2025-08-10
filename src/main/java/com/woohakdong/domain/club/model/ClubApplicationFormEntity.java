package com.woohakdong.domain.club.model;

import com.woohakdong.domain.club.infrastructure.storage.FormQuestionListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "club_application_form")
public class ClubApplicationFormEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @Column(name = "name")
    private String name;

    @Convert(converter = FormQuestionListConverter.class)
    @Column(name = "form_content", columnDefinition = "TEXT")
    private List<FormQuestion> formContent;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @Column(name = "application_count")
    private Integer applicationCount;

    public static ClubApplicationFormEntity create(ClubApplicationFormCreateCommand command, ClubEntity club) {
        return new ClubApplicationFormEntity(
                null,
                club,
                command.name(),
                command.formContent(),
                LocalDate.now(),
                0
        );
    }

    public void addSubmission() {
        applicationCount++;
    }
}
