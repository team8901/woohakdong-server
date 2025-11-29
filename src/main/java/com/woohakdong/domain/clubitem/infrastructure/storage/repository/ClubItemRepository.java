package com.woohakdong.domain.clubitem.infrastructure.storage.repository;

import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubItemRepository extends JpaRepository<ClubItemEntity, Long> {

    List<ClubItemEntity> findByClubIdAndDeletedFalse(Long clubId);

    @Query("SELECT i FROM ClubItemEntity i WHERE i.club.id = :clubId " +
            "AND i.deleted = false " +
            "AND (:keyword IS NULL OR i.name LIKE %:keyword%) " +
            "AND (:category IS NULL OR i.category = :category)")
    List<ClubItemEntity> findByClubIdWithFilters(
            @Param("clubId") Long clubId,
            @Param("keyword") String keyword,
            @Param("category") ClubItemCategory category
    );

    Optional<ClubItemEntity> findByIdAndDeletedFalse(Long id);

    Optional<ClubItemEntity> findByIdAndClubIdAndDeletedFalse(Long id, Long clubId);
}
