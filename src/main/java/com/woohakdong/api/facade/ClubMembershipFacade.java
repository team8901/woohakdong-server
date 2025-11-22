package com.woohakdong.api.facade;

import com.woohakdong.api.dto.response.ClubMembershipResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.club.application.ClubService;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClubMembershipFacade {

    private final ClubService clubService;

    public ListWrapper<ClubMembershipResponse> getClubMembers(Long clubId) {
        List<ClubMembershipEntity> members = clubService.getClubMembers(clubId);
        return ListWrapper.of(members.stream()
                .map(ClubMembershipResponse::of)
                .toList());
    }
}
