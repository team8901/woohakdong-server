package com.woohakdong.domain.schedule.infrastructure.storage;

import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.schedule.model.ScheduleEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    @EntityGraph(attributePaths = {"writer"})
    List<ScheduleEntity> findAllByClubAndDeletedAtIsNullOrderByStartTimeAsc(ClubEntity club);

    @EntityGraph(attributePaths = {"writer"})
    Optional<ScheduleEntity> findByIdAndClubAndDeletedAtIsNull(Long id, ClubEntity club);
}
