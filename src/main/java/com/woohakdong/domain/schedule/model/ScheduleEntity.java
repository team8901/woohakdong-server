package com.woohakdong.domain.schedule.model;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "schedule")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String color;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private ClubMembershipEntity writer;

    public static ScheduleEntity create(ScheduleCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        return new ScheduleEntity(
                null,
                command.title(),
                command.content(),
                command.color(),
                command.startTime(),
                command.endTime(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                club,
                writer
        );
    }

    public void update(ScheduleUpdateCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.color = command.color();
        this.startTime = command.startTime();
        this.endTime = command.endTime();
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
