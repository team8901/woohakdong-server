package com.woohakdong.domain.clubitem.domain;

import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemHistoryRepository;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemRepository;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubItemService {

    private final ClubItemRepository clubItemRepository;
    private final ClubItemHistoryRepository clubItemHistoryRepository;

    public List<ClubItemEntity> getClubItems(Long clubId, String keyword, ClubItemCategory category) {
        return clubItemRepository.findByClubIdWithFilters(clubId, keyword, category);
    }

    public List<ClubItemHistoryEntity> getClubItemHistory(Long clubId) {
        return clubItemHistoryRepository.findByClubIdWithDetails(clubId);
    }
}
