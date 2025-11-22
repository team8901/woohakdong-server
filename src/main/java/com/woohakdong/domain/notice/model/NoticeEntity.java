package com.woohakdong.domain.notice.model;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "notice")
public class NoticeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean isPinned;

    @Column(nullable = false)
    private LocalDate updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private ClubMembershipEntity writer;

    public static NoticeEntity create(NoticeCreateCommand command, ClubEntity club, ClubMembershipEntity writer) {
        return new NoticeEntity(
                null,
                command.title(),
                command.content(),
                command.isPinned(),
                LocalDate.now(),
                club,
                writer
        );
    }

    public void update(NoticeUpdateCommand command) {
        this.title = command.title();
        this.content = command.content();
        this.isPinned = command.isPinned();
        this.updatedAt = LocalDate.now();
    }
}
