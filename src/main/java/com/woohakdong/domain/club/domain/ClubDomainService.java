package com.woohakdong.domain.club.domain;

import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.woohakdong.exception.CustomErrorInfo.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubDomainService {

    private final ClubRepository clubRepository;
    private final ClubMemberShipRepository clubMemberShipRepository;

    @Transactional
    public Long registerNewClub(ClubRegisterCommand command) {
        ClubEntity newClub = ClubEntity.create(command, LocalDate.now());
        ClubEntity savedClub = clubRepository.save(newClub);
        return savedClub.getId();
    }

    @Transactional
    public void assignClubOwner(Long clubId, UserProfileEntity userProfile) {
        ClubEntity club = clubRepository.findById(clubId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB)
        );

        if (clubMemberShipRepository.existsByClubAndUserProfile(club, userProfile)) {
            throw new CustomException(CONFLICT_ALREADY_JOINED_CLUB);
        }

        ClubMembershipEntity clubMembership = ClubMembershipEntity.createClubOwner(club, userProfile);
        clubMemberShipRepository.save(clubMembership);

        club.updateOwner(clubMembership);
        clubRepository.save(club);
    }

    public List<ClubEntity> findJoinedClubs(UserProfileEntity userProfile) {
        List<ClubMembershipEntity> joinedClubMemberShip = clubMemberShipRepository.findAllByUserProfile(userProfile);

        // TODO : querydsl로 변경
        return joinedClubMemberShip.stream()
                .map(ClubMembershipEntity::getClub)
                .toList();
    }

    public ClubEntity getById(Long clubId) {
        return clubRepository.findById(clubId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB)
        );
    }

    public void updateClub(ClubEntity club) {
        clubRepository.save(club);
    }

    public ClubMembershipEntity getClubMembership(UserProfileEntity userProfile) {
        return clubMemberShipRepository.findByUserProfile(userProfile).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB_MEMBERSHIP)
        );
    }
}
