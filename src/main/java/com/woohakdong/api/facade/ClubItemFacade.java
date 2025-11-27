package com.woohakdong.api.facade;

import com.woohakdong.api.dto.response.ClubItemHistoryResponse;
import com.woohakdong.api.dto.response.ClubItemResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.clubitem.domain.ClubItemService;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubItemFacade {

    private final ClubItemService clubItemService;

    public ListWrapper<ClubItemResponse> getClubItems(Long clubId, String keyword, ClubItemCategory category) {
        List<ClubItemEntity> items = clubItemService.getClubItems(clubId, keyword, category);
        return ListWrapper.of(items.stream()
                .map(ClubItemResponse::from)
                .toList());
    }

    public ListWrapper<ClubItemHistoryResponse> getClubItemHistory(Long clubId) {
        List<ClubItemHistoryEntity> histories = clubItemService.getClubItemHistory(clubId);
        return ListWrapper.of(histories.stream()
                .map(ClubItemHistoryResponse::from)
                .toList());
    }
}
