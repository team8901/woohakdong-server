package com.woohakdong.domain.club.domain;

import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubRegistrationPolicy {

    private final ClubRepository clubRepository;

    public void validateClubRegistration(ClubRegisterCommand command) {
        if (clubRepository.existsByNameEn(command.nameEn())) {
            // TODO : 커스텀 에러로 변경
            throw new IllegalArgumentException("이미 존재하는 클럽 영문 이름입니다.");
        }
    }
}
