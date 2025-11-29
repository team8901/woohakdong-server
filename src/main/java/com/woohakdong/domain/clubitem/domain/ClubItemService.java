package com.woohakdong.domain.clubitem.domain;

import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_CLUB_ITEM;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemHistoryRepository;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemRepository;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import com.woohakdong.domain.clubitem.model.ClubItemRegisterCommand;
import com.woohakdong.domain.clubitem.model.ClubItemUpdateCommand;
import com.woohakdong.exception.CustomException;
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
    private final ClubDomainService clubDomainService;

    public List<ClubItemEntity> getClubItems(Long clubId, String keyword, ClubItemCategory category) {
        return clubItemRepository.findByClubIdWithFilters(clubId, keyword, category);
    }

    public List<ClubItemHistoryEntity> getClubItemHistory(Long clubId) {
        return clubItemHistoryRepository.findByClubIdWithDetails(clubId);
    }

    @Transactional
    public Long registerClubItem(Long clubId, ClubItemRegisterCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);

        ClubItemEntity clubItem = ClubItemEntity.create(
                club,
                command.name(),
                command.photo(),
                command.description(),
                command.location(),
                command.category(),
                command.rentalMaxDay()
        );

        ClubItemEntity savedItem = clubItemRepository.save(clubItem);
        return savedItem.getId();
    }

    @Transactional
    public void updateClubItem(Long clubId, Long itemId, ClubItemUpdateCommand command) {
        ClubItemEntity clubItem = clubItemRepository.findByIdAndClubIdAndDeletedFalse(itemId, clubId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_ITEM));

        clubItem.update(command);
    }

    @Transactional
    public void deleteClubItem(Long clubId, Long itemId) {
        ClubItemEntity clubItem = clubItemRepository.findByIdAndClubIdAndDeletedFalse(itemId, clubId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_ITEM));

        clubItem.delete();
    }
}
