package com.woohakdong.api.facade;

import com.woohakdong.api.dto.request.ClubApplicationFormCreateRequest;
import com.woohakdong.api.dto.request.ClubApplicationSubmissionRequest;
import com.woohakdong.api.dto.request.ClubNameValidateRequest;
import com.woohakdong.api.dto.request.ClubRegisterRequest;
import com.woohakdong.api.dto.request.ClubUpdateRequest;
import com.woohakdong.api.dto.response.ClubApplicationFormIdResponse;
import com.woohakdong.api.dto.response.ClubApplicationFormInfoResponse;
import com.woohakdong.api.dto.response.ClubApplicationSubmissionIdResponse;
import com.woohakdong.api.dto.response.ClubApplicationSubmissionResponse;
import com.woohakdong.api.dto.response.ClubIdResponse;
import com.woohakdong.api.dto.response.ClubInfoResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.domain.club.application.ClubService;
import com.woohakdong.domain.club.model.ClubApplicationFormCreateCommand;
import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionCommand;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubInfoSearchQuery;
import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.club.model.ClubUpdateCommand;
import com.woohakdong.domain.user.application.UserService;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClubFacade {

    private final UserService userService;
    private final ClubService clubService;

    public ClubIdResponse registerNewClub(Long userAuthId, ClubRegisterRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        ClubRegisterCommand command = request.toCommand();
        return ClubIdResponse.of(clubService.registerNewClub(command, userProfile));
    }

    public ListWrapper<ClubInfoResponse> getJoinedClubs(Long userAuthId) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        List<ClubEntity> clubs = clubService.getJoinedClubs(userProfile);

        return ListWrapper.of(clubs.stream()
                .map(ClubInfoResponse::of)
                .toList());
    }

    public void validateClubName(ClubNameValidateRequest request) {
        ClubNameValidateQuery query = request.toQueryModel();
        clubService.validateClubName(query);
    }

    public void updateClubInfo(Long userAuthId, Long clubId, ClubUpdateRequest request) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        ClubUpdateCommand command = request.toCommand();
        clubService.updateClubInfo(userProfile, command, clubId);
    }

    public ListWrapper<ClubInfoResponse> searchClubs(String name, String nameEn) {
        ClubInfoSearchQuery query = ClubInfoSearchQuery.of(name, nameEn);
        List<ClubEntity> clubs = clubService.searchClubs(query);
        return ListWrapper.of(clubs.stream()
                .map(ClubInfoResponse::of)
                .toList());
    }

    public ClubApplicationFormIdResponse createClubApplicationForm(Long clubId, Long userAuthId,
                                                                   ClubApplicationFormCreateRequest request) {
        ClubApplicationFormCreateCommand command = request.toCommandModel();
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        Long clubApplicationFormId = clubService.createClubApplicationForm(clubId, userProfile, command);
        return ClubApplicationFormIdResponse.of(clubApplicationFormId);
    }

    public ClubApplicationFormInfoResponse getLatestClubApplicationForm(Long clubId) {
        ClubApplicationFormEntity clubApplicationForm = clubService.getLatestClubApplicationForm(clubId);
        return ClubApplicationFormInfoResponse.of(clubApplicationForm);
    }

    public ListWrapper<ClubApplicationFormInfoResponse> getAllClubApplicationForms(Long clubId) {
        List<ClubApplicationFormEntity> clubApplicationFormEntities = clubService.getAllClubApplicationForms(clubId);
        return ListWrapper.of(clubApplicationFormEntities.stream()
                .map(ClubApplicationFormInfoResponse::of)
                .toList());
    }

    public ClubApplicationSubmissionIdResponse submitClubApplicationForm(Long clubId, Long applicationFormId,
                                                                         Long userAuthId, ClubApplicationSubmissionRequest request) {
        ClubApplicationSubmissionCommand command = request.toCommandModel();
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        Long submissionId = clubService.submitClubApplicationForm(clubId, applicationFormId, userProfile, command);
        return ClubApplicationSubmissionIdResponse.of(submissionId);
    }

    public ClubApplicationSubmissionResponse getMyClubApplicationSubmission(Long clubId, Long applicationFormId, Long userAuthId) {
        UserProfileEntity userProfile = userService.getProfileWithAuthId(userAuthId);
        ClubApplicationSubmissionEntity clubApplicationSubmission = clubService.getMyClubApplicationSubmission(clubId, applicationFormId, userProfile);
        return ClubApplicationSubmissionResponse.of(clubApplicationSubmission);
    }
}
