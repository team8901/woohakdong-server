package com.woohakdong.domain.clubitem.infrastructure.storage.repository;

import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubItemHistoryRepository extends JpaRepository<ClubItemHistoryEntity, Long> {

    @Query("SELECT h FROM ClubItemHistoryEntity h " +
            "JOIN FETCH h.clubItem i " +
            "JOIN FETCH h.clubMembership m " +
            "WHERE i.club.id = :clubId " +
            "ORDER BY h.rentalDate DESC")
    List<ClubItemHistoryEntity> findByClubIdWithDetails(@Param("clubId") Long clubId);

    @Query("SELECT h FROM ClubItemHistoryEntity h " +
            "WHERE h.clubItem.id = :itemId " +
            "AND h.returnDate IS NULL")
    Optional<ClubItemHistoryEntity> findActiveRentalByItemId(@Param("itemId") Long itemId);
}
