package com.woohakdong.domain.clubitem.domain;

import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ITEM_ALREADY_RENTED;
import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ITEM_NOT_AVAILABLE;
import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ITEM_NOT_RENTED;
import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_RENTAL_DAYS_EXCEEDED;
import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_CLUB_ITEM;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemHistoryRepository;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemRepository;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import com.woohakdong.domain.clubitem.model.ClubItemRegisterCommand;
import com.woohakdong.domain.clubitem.model.ClubItemUpdateCommand;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
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

    public ClubItemEntity getClubItem(Long clubId, Long itemId) {
        return clubItemRepository.findByIdAndClubIdAndDeletedFalse(itemId, clubId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_ITEM));
    }

    public ClubItemHistoryEntity getActiveRental(Long itemId) {
        return clubItemHistoryRepository.findActiveRentalByItemId(itemId).orElse(null);
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

    @Transactional
    public ClubItemHistoryEntity rentClubItem(Long clubId, Long itemId, ClubMembershipEntity membership, Integer rentalDays) {
        ClubItemEntity clubItem = clubItemRepository.findByIdAndClubIdAndDeletedFalse(itemId, clubId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_ITEM));

        // 대여 가능 여부 확인
        if (!clubItem.getAvailable()) {
            throw new CustomException(CONFLICT_ITEM_NOT_AVAILABLE);
        }

        // 이미 대여 중인지 확인
        if (clubItem.getUsing()) {
            throw new CustomException(CONFLICT_ITEM_ALREADY_RENTED);
        }

        // 최대 대여 일수 확인
        if (rentalDays > clubItem.getRentalMaxDay()) {
            throw new CustomException(CONFLICT_RENTAL_DAYS_EXCEEDED);
        }

        LocalDate rentalDate = LocalDate.now();
        LocalDate dueDate = rentalDate.plusDays(rentalDays);

        // 물품 상태 변경
        clubItem.rent(rentalDate, rentalDays);

        // 대여 이력 생성
        ClubItemHistoryEntity history = ClubItemHistoryEntity.create(
                clubItem,
                membership,
                rentalDate,
                dueDate
        );

        return clubItemHistoryRepository.save(history);
    }

    @Transactional
    public void returnClubItem(Long clubId, Long itemId, String returnImage) {
        ClubItemEntity clubItem = clubItemRepository.findByIdAndClubIdAndDeletedFalse(itemId, clubId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_ITEM));

        // 대여 중인지 확인
        if (!clubItem.getUsing()) {
            throw new CustomException(CONFLICT_ITEM_NOT_RENTED);
        }

        // 현재 대여 이력 찾기
        ClubItemHistoryEntity history = clubItemHistoryRepository.findActiveRentalByItemId(itemId)
                .orElseThrow(() -> new CustomException(CONFLICT_ITEM_NOT_RENTED));

        // 반납 처리
        history.returnItem(LocalDate.now(), returnImage);
        clubItem.returnItem();
    }
}
