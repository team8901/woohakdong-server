package com.woohakdong.domain.club.domain;

import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ALREADY_EXISTING_CLUB_NAME_EN;

import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubRegistrationPolicy {

    private final ClubRepository clubRepository;

    public void validateClubRegistration(ClubRegisterCommand command) {
        if (clubRepository.existsByNameEn(command.nameEn())) {
            throw new CustomException(CONFLICT_ALREADY_EXISTING_CLUB_NAME_EN);
        }
    }
}
