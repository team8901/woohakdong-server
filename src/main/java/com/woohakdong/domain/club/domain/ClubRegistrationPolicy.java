package com.woohakdong.domain.club.domain;

import static com.woohakdong.exception.CustomErrorInfo.CONFLICT_ALREADY_EXISTING_CLUB_NAME;

import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubRegistrationPolicy {

    private final ClubRepository clubRepository;

    /**
     * 동아리 국문, 영문 이름은 중복될 수 없다.
     */
    public void validateClubName(String name, String nameEn) {
        if (clubRepository.existsByNameOrNameEn(name, nameEn)) {
            throw new CustomException(CONFLICT_ALREADY_EXISTING_CLUB_NAME);
        }
    }
}
