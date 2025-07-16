package com.woohakdong.domain.club.domain;

import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
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
                // TODO : 커스텀 에러로 변경
                () -> new IllegalArgumentException("존재하지 않는 동아리입니다.")
        );

        ClubMembershipEntity clubMembership = ClubMembershipEntity.createClubOwner(club, userProfile);
        if (clubMemberShipRepository.existsByClubAndUserProfile(club, userProfile)) {
            // TODO : 커스텀 에러로 변경
            throw new IllegalArgumentException("이미 동아리의 회원입니다.");
        }

        clubMemberShipRepository.save(clubMembership);
        club.updateOwner(userProfile);
    }
}
