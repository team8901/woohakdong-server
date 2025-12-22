package com.woohakdong.domain.activity.infrastructure.storage;

import com.woohakdong.domain.activity.model.ActivityEntity;
import com.woohakdong.domain.club.model.ClubEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    @EntityGraph(attributePaths = {"writer"})
    List<ActivityEntity> findAllByClubAndDeletedAtIsNullOrderByActivityDateDescCreatedAtDesc(ClubEntity club);

    @EntityGraph(attributePaths = {"writer"})
    Optional<ActivityEntity> findByIdAndClubAndDeletedAtIsNull(Long id, ClubEntity club);
}
