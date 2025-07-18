package com.woohakdong.domain.club.domain;

import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ALREADY_JOINED_CLUB;
import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_CLUB;

import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubDomainService {

    private final ClubRepository clubRepository;
    private final ClubMemberShipRepository clubMemberShipRepository;

    public Long registerNewClub(ClubRegisterCommand command) {
        ClubEntity newClub = ClubEntity.create(command, LocalDate.now());
        ClubEntity savedClub = clubRepository.save(newClub);
        return savedClub.getId();
    }

    public void assignClubOwner(Long clubId, UserProfileEntity userProfile) {
        ClubEntity club = clubRepository.findById(clubId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB)
        );

        ClubMembershipEntity clubMembership = ClubMembershipEntity.createClubOwner(club, userProfile);
        if (clubMemberShipRepository.existsByClubAndUserProfile(club, userProfile)) {
            throw new CustomException(CONFLICT_ALREADY_JOINED_CLUB);
        }

        clubMemberShipRepository.save(clubMembership);
        club.updateOwner(userProfile);
    }
}
