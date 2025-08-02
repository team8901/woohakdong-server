package com.woohakdong.domain.club.application;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.domain.ClubInformationPolicy;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubInfoSearchQuery;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.club.model.ClubUpdateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubApplicationService {

    private final ClubDomainService clubDomainService;
    private final ClubInformationPolicy clubInformationPolicy;

    @Transactional
    public Long registerNewClub(ClubRegisterCommand command, UserProfileEntity userProfile) {
        clubInformationPolicy.validateClubName(command.name(), command.nameEn());
        Long clubId = clubDomainService.registerNewClub(command);
        clubDomainService.assignClubOwner(clubId, userProfile);
        return clubId;
    }

    public List<ClubEntity> getJoinedClubs(UserProfileEntity userProfile) {
        return clubDomainService.findJoinedClubs(userProfile);
    }

    public void validateClubName(ClubNameValidateQuery query) {
        clubInformationPolicy.validateClubName(query.name(), query.nameEn());
    }

    @Transactional
    public void updateClubInfo(UserProfileEntity userProfile, ClubUpdateCommand command, Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity clubMembership = clubDomainService.getClubMembership(userProfile);
        club.verifyOwner(clubMembership);
        club.updateInfo(command);
        clubDomainService.updateClub(club);
    }

    public List<ClubEntity> searchClubs(ClubInfoSearchQuery query) {
        return clubDomainService.searchClubs(query);
    }
}
