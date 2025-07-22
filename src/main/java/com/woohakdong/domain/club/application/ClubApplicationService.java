package com.woohakdong.domain.club.application;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.domain.ClubRegistrationPolicy;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubApplicationService {

    private final ClubDomainService clubDomainService;
    private final ClubRegistrationPolicy clubRegistrationPolicy;

    @Transactional
    public Long registerNewClub(ClubRegisterCommand command, UserProfileEntity userProfile) {
        clubRegistrationPolicy.validateClubName(command.name(), command.nameEn());
        Long clubId = clubDomainService.registerNewClub(command);
        clubDomainService.assignClubOwner(clubId, userProfile);
        return clubId;
    }

    public List<ClubEntity> getJoinedClubs(UserProfileEntity userProfile) {
        return clubDomainService.findJoinedClubs(userProfile);
    }

    public void validateClubName(ClubNameValidateQuery query) {
        clubRegistrationPolicy.validateClubName(query.name(), query.nameEn());
    }
}
