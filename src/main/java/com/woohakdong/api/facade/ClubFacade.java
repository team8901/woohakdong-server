package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.ClubNameValidateRequest;
import com.woohakdong.api.dto.request.ClubRegisterRequest;
import com.woohakdong.api.dto.request.ClubUpdateRequest;
import com.woohakdong.api.dto.response.ClubIdResponse;
import com.woohakdong.api.dto.response.ClubInfoResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.club.application.ClubApplicationService;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.club.model.ClubUpdateCommand;
import com.woohakdong.domain.user.application.UserApplicationService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubFacade {

    private final UserApplicationService userApplicationService;
    private final ClubApplicationService clubApplicationService;

    public ClubIdResponse registerNewClub(Long userAuthId, ClubRegisterRequest request) {
        UserProfileEntity userProfile = userApplicationService.getProfileWithAuthId(userAuthId);
        ClubRegisterCommand command = request.toCommand();
        return ClubIdResponse.of(clubApplicationService.registerNewClub(command, userProfile));
    }

    public ListWrapper<ClubInfoResponse> getJoinedClubs(Long userAuthId) {
        UserProfileEntity userProfile = userApplicationService.getProfileWithAuthId(userAuthId);
        List<ClubEntity> clubs = clubApplicationService.getJoinedClubs(userProfile);

        return ListWrapper.of(clubs.stream()
                .map(ClubInfoResponse::of)
                .toList());
    }

    public void validateClubName(ClubNameValidateRequest request) {
        ClubNameValidateQuery query = request.toQueryModel();
        clubApplicationService.validateClubName(query);
    }

    public void updateClubInfo(Long userAuthId, Long clubId, ClubUpdateRequest request) {
        UserProfileEntity userProfile = userApplicationService.getProfileWithAuthId(userAuthId);
        ClubUpdateCommand command = request.toCommand();
        clubApplicationService.updateClubInfo(userProfile, command, clubId);
    }
}
