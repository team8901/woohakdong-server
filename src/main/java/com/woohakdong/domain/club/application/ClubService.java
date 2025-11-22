package com.woohakdong.domain.club.application;

import com.woohakdong.domain.club.domain.ClubDomainService;
import com.woohakdong.domain.club.domain.ClubInformationPolicy;
import com.woohakdong.domain.club.infrastructure.storage.ClubApplicationFormRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubApplicationSubmissionRepository;
import com.woohakdong.domain.club.model.ClubApplicationFormCreateCommand;
import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionCommand;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubInfoSearchQuery;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubNameValidateQuery;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.club.model.ClubUpdateCommand;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.woohakdong.exception.CustomErrorInfo.NOT_FOUND_CLUB_APPLICATION_FORM;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {

    private final ClubDomainService clubDomainService;
    private final ClubInformationPolicy clubInformationPolicy;
    private final ClubApplicationFormRepository clubApplicationFormRepository;
    private final ClubApplicationSubmissionRepository clubApplicationSubmissionRepository;

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

    @Transactional
    public Long createClubApplicationForm(Long clubId, UserProfileEntity userProfile,
                                          ClubApplicationFormCreateCommand command) {
        ClubEntity club = clubDomainService.getById(clubId);
        ClubMembershipEntity clubMembership = clubDomainService.getClubMembership(userProfile);
        club.verifyOwner(clubMembership);

        ClubApplicationFormEntity clubApplicationForm = ClubApplicationFormEntity.create(command, club);
        ClubApplicationFormEntity savedForm = clubApplicationFormRepository.save(clubApplicationForm);
        return savedForm.getId();
    }

    public ClubApplicationFormEntity getLatestClubApplicationForm(Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return clubApplicationFormRepository.findTopByClubOrderByCreatedAtDesc(club)
                .orElseThrow(() -> new CustomException(NOT_FOUND_CLUB_APPLICATION_FORM));
    }

    public List<ClubApplicationFormEntity> getAllClubApplicationForms(Long clubId) {
        ClubEntity club = clubDomainService.getById(clubId);
        return clubApplicationFormRepository.findAllByClubOrderByCreatedAtDesc(club);
    }

    @Transactional
    public Long submitClubApplicationForm(Long clubId, Long applicationFormId, UserProfileEntity userProfile, ClubApplicationSubmissionCommand command) {
        ClubApplicationFormEntity clubApplicationForm = clubApplicationFormRepository.findById(applicationFormId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB_APPLICATION_FORM)
        );

        ClubApplicationSubmissionEntity clubApplicationSubmission = ClubApplicationSubmissionEntity.create(
                command,
                clubApplicationForm,
                userProfile
        );
        ClubApplicationSubmissionEntity savedSubmission = clubApplicationSubmissionRepository.save(clubApplicationSubmission);

        clubApplicationForm.addSubmission();
        clubApplicationFormRepository.save(clubApplicationForm);
        return savedSubmission.getId();
    }

    public ClubApplicationSubmissionEntity getMyClubApplicationSubmission(Long clubId, Long applicationFormId, UserProfileEntity userProfile) {
        ClubApplicationFormEntity clubApplicationForm = clubApplicationFormRepository.findById(applicationFormId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB_APPLICATION_FORM)
        );

        return clubApplicationSubmissionRepository.findByClubApplicationFormAndUserProfile(clubApplicationForm, userProfile).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB_APPLICATION_FORM)
        );
    }

    public List<ClubApplicationSubmissionEntity> getClubApplicationSubmissions(Long clubId, Long applicationFormId, UserProfileEntity userProfile) {
        ClubEntity club = clubDomainService.getById(clubId);

        // Club Owner만 신청서 열람이 가능하다.
        ClubMembershipEntity clubMembership = clubDomainService.getClubMembership(userProfile);
        club.verifyOwner(clubMembership);

        clubApplicationFormRepository.findById(applicationFormId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CLUB_APPLICATION_FORM)
        );

        return clubApplicationSubmissionRepository.findAllByClubApplicationFormId(applicationFormId);
    }

    public List<ClubMembershipEntity> getClubMembers(Long clubId) {
        return clubDomainService.getClubMembers(clubId);
    }

    public ClubMembershipEntity getClubMember(Long clubId, Long clubMembershipId) {
        return clubDomainService.getClubMember(clubId, clubMembershipId);
    }
}
