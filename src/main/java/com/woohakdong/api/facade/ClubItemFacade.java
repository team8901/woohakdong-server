package com.woohakdong.api.facade;

import com.woohakdong.api.dto.response.ClubItemHistoryResponse;
import com.woohakdong.api.dto.response.ClubItemIdResponse;
import com.woohakdong.api.dto.response.ClubItemRentResponse;
import com.woohakdong.api.dto.response.ClubItemResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.clubitem.domain.ClubItemService;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import com.woohakdong.domain.clubitem.model.ClubItemRegisterCommand;
import com.woohakdong.domain.clubitem.model.ClubItemUpdateCommand;
import com.woohakdong.domain.user.application.UserService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubItemFacade {

    private final ClubItemService clubItemService;
    private final UserService userService;
    private final ClubDomainService clubDomainService;

    public ListWrapper<ClubItemResponse> getClubItems(Long clubId, String keyword, ClubItemCategory category) {
        List<ClubItemEntity> items = clubItemService.getClubItems(clubId, keyword, category);
        return ListWrapper.of(items.stream()
                .map(ClubItemResponse::from)
                .toList());
    }

    public ClubItemIdResponse addClubItem(Long clubId, ClubItemRegisterCommand command) {
        Long clubItemId = clubItemService.registerClubItem(clubId, command);
        return ClubItemIdResponse.of(clubItemId);
    }

    public void updateClubItem(Long clubId, Long itemId, ClubItemUpdateCommand command) {
        clubItemService.updateClubItem(clubId, itemId, command);
    }

    public void deleteClubItem(Long clubId, Long itemId) {
        clubItemService.deleteClubItem(clubId, itemId);
    }

    public ClubItemRentResponse rentClubItem(Long clubId, Long itemId, Long userAuthId, Integer rentalDays) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        ClubMembershipEntity membership = clubDomainService.getClubMemberByUserProfile(clubId, userProfile);

        ClubItemHistoryEntity history = clubItemService.rentClubItem(clubId, itemId, membership, rentalDays);

        return ClubItemRentResponse.of(history.getId(), history.getRentalDate(), history.getDueDate());
    }

    public void returnClubItem(Long clubId, Long itemId, String returnImage) {
        clubItemService.returnClubItem(clubId, itemId, returnImage);
    }

    public ListWrapper<ClubItemHistoryResponse> getClubItemHistory(Long clubId) {
        List<ClubItemHistoryEntity> histories = clubItemService.getClubItemHistory(clubId);
        return ListWrapper.of(histories.stream()
                .map(ClubItemHistoryResponse::from)
                .toList());
    }
}
