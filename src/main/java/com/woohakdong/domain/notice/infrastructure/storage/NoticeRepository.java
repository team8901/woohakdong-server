package com.woohakdong.domain.notice.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.notice.model.NoticeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

    @EntityGraph(attributePaths = {"writer"})
    List<NoticeEntity> findAllByClubOrderByIsPinnedDescUpdatedAtDesc(ClubEntity club);

    @EntityGraph(attributePaths = {"writer"})
    Optional<NoticeEntity> findByIdAndClub(Long id, ClubEntity club);
}
