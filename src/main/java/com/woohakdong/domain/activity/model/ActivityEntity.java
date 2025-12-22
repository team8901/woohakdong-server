package com.woohakdong.domain.activity.model;

import com.woohakdong.domain.activity.infrastructure.storage.ActivityImageListConverter;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "activity")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private Integer participantCount;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityTag tag;

    @Convert(converter = ActivityImageListConverter.class)
    @Column(columnDefinition = "JSON")
    private List<String> activityImages;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private ClubMembershipEntity writer;

    public static ActivityEntity create(ActivityCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        return new ActivityEntity(
                null,
                command.title(),
                command.content(),
                command.participantCount(),
                command.activityDate(),
                command.tag(),
                command.activityImages(),
                LocalDate.now(),
                LocalDate.now(),
                null,
                club,
                writer
        );
    }

    public void update(ActivityUpdateCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.participantCount = command.participantCount();
        this.activityDate = command.activityDate();
        this.tag = command.tag();
        this.activityImages = command.activityImages();
        this.updatedAt = LocalDate.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
