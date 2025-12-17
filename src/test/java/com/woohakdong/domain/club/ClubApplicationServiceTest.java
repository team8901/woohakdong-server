package com.woohakdong.domain.club;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.firebase.auth.FirebaseAuth;
import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.domain.club.application.ClubService;
import com.woohakdong.domain.club.infrastructure.storage.ClubApplicationFormRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubApplicationSubmissionRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubApplicationFormEntity;
import com.woohakdong.domain.club.model.ClubApplicationStatus;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMemberRole;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.user.infrastructure.storage.repository.UserProfileRepository;
import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClubApplicationServiceTest {

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ClubService clubService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMemberShipRepository clubMemberShipRepository;

    @Autowired
    private ClubApplicationFormRepository clubApplicationFormRepository;

    @Autowired
    private ClubApplicationSubmissionRepository clubApplicationSubmissionRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    private ClubEntity club;
    private UserProfileEntity ownerProfile;
    private UserProfileEntity applicantProfile;
    private ClubApplicationFormEntity applicationForm;
    private ClubApplicationSubmissionEntity submission;

    @BeforeEach
    void setUp() {
        // 데이터 정리
        clubApplicationSubmissionRepository.deleteAll();
        clubApplicationFormRepository.deleteAll();
        clubMemberShipRepository.deleteAll();
        clubRepository.deleteAll();
        userProfileRepository.deleteAll();
        userAuthRepository.deleteAll();

        // 동아리 회장 (Owner) 생성
        UserAuthEntity ownerAuth = userAuthRepository.save(
                new UserAuthEntity(null, "회장", "owner@test.com", UserAuthRole.USER, "google", "owner-uid")
        );
        ownerProfile = userProfileRepository.save(
                new UserProfileEntity(null, "회장", "회장닉네임", "owner@test.com",
                        "010-1111-1111", "20210001", "컴퓨터공학과", Gender.MALE, ownerAuth)
        );

        // 가입 신청자 생성
        UserAuthEntity applicantAuth = userAuthRepository.save(
                new UserAuthEntity(null, "신청자", "applicant@test.com", UserAuthRole.USER, "google", "applicant-uid")
        );
        applicantProfile = userProfileRepository.save(
                new UserProfileEntity(null, "신청자", "신청자닉네임", "applicant@test.com",
                        "010-2222-2222", "20210002", "소프트웨어학과", Gender.FEMALE, applicantAuth)
        );

        // 동아리 생성
        ClubRegisterCommand clubCommand = new ClubRegisterCommand(
                "테스트동아리", "testclub", "테스트입니다",
                null, null, "101호", null, null, 10000
        );
        club = clubRepository.save(ClubEntity.create(clubCommand, LocalDate.now()));

        // 회장 멤버십 생성 및 동아리에 연결
        ClubMembershipEntity ownerMembership = clubMemberShipRepository.save(
                ClubMembershipEntity.createClubOwner(club, ownerProfile)
        );
        club.updateOwner(ownerMembership);
        clubRepository.save(club);

        // 신청 폼 생성
        applicationForm = clubApplicationFormRepository.save(
                new ClubApplicationFormEntity(null, club, "가입신청폼", List.of(), LocalDate.now(), 0)
        );

        // 가입 신청서 생성 (PENDING 상태)
        submission = clubApplicationSubmissionRepository.save(
                new ClubApplicationSubmissionEntity(null, applicationForm, List.of(),
                        applicantProfile, LocalDate.now(), ClubApplicationStatus.PENDING, null, null)
        );
    }

    @Nested
    @DisplayName("approveClubApplication() 테스트")
    class ApproveTest {

        @Test
        @DisplayName("회장이 가입 신청을 승인하면 신청자가 회원으로 등록된다")
        void approveSuccess() {
            // when
            clubService.approveClubApplication(club.getId(), applicationForm.getId(),
                    submission.getId(), ownerProfile);

            // then
            ClubApplicationSubmissionEntity updatedSubmission =
                    clubApplicationSubmissionRepository.findById(submission.getId()).orElseThrow();
            assertThat(updatedSubmission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.APPROVED);
            assertThat(updatedSubmission.getProcessedAt()).isEqualTo(LocalDate.now());

            // 회원으로 등록되었는지 확인
            boolean isMember = clubMemberShipRepository.existsByClubAndUserProfile(club, applicantProfile);
            assertThat(isMember).isTrue();

            // 회원 역할이 MEMBER인지 확인
            ClubMembershipEntity membership = clubMemberShipRepository
                    .findByClubAndUserProfile(club, applicantProfile).orElseThrow();
            assertThat(membership.getClubMemberRole()).isEqualTo(ClubMemberRole.MEMBER);
        }

        @Test
        @DisplayName("회장이 아닌 사람이 승인하면 예외가 발생한다")
        void approveByNonOwner() {
            // given - 신청자를 회원으로 먼저 등록
            clubMemberShipRepository.save(ClubMembershipEntity.createMember(club, applicantProfile));

            // when & then - 일반 회원이 승인 시도
            assertThatThrownBy(() -> clubService.approveClubApplication(
                    club.getId(), applicationForm.getId(), submission.getId(), applicantProfile))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getStatusCode()).isEqualTo(403);
                    });
        }

        @Test
        @DisplayName("이미 승인된 신청서를 다시 승인하면 예외가 발생한다")
        void approveAlreadyApproved() {
            // given - 먼저 승인
            clubService.approveClubApplication(club.getId(), applicationForm.getId(),
                    submission.getId(), ownerProfile);

            // when & then
            assertThatThrownBy(() -> clubService.approveClubApplication(
                    club.getId(), applicationForm.getId(), submission.getId(), ownerProfile))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getStatusCode()).isEqualTo(409);
                    });
        }
    }

    @Nested
    @DisplayName("rejectClubApplication() 테스트")
    class RejectTest {

        @Test
        @DisplayName("회장이 가입 신청을 거절하면 상태가 REJECTED로 변경된다")
        void rejectSuccess() {
            // given
            String reason = "서류 미비";

            // when
            clubService.rejectClubApplication(club.getId(), applicationForm.getId(),
                    submission.getId(), ownerProfile, reason);

            // then
            ClubApplicationSubmissionEntity updatedSubmission =
                    clubApplicationSubmissionRepository.findById(submission.getId()).orElseThrow();
            assertThat(updatedSubmission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.REJECTED);
            assertThat(updatedSubmission.getRejectionReason()).isEqualTo(reason);
            assertThat(updatedSubmission.getProcessedAt()).isEqualTo(LocalDate.now());

            // 회원으로 등록되지 않았는지 확인
            boolean isMember = clubMemberShipRepository.existsByClubAndUserProfile(club, applicantProfile);
            assertThat(isMember).isFalse();
        }

        @Test
        @DisplayName("거절 사유 없이 거절해도 정상 처리된다")
        void rejectWithoutReason() {
            // when
            clubService.rejectClubApplication(club.getId(), applicationForm.getId(),
                    submission.getId(), ownerProfile, null);

            // then
            ClubApplicationSubmissionEntity updatedSubmission =
                    clubApplicationSubmissionRepository.findById(submission.getId()).orElseThrow();
            assertThat(updatedSubmission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.REJECTED);
            assertThat(updatedSubmission.getRejectionReason()).isNull();
        }

        @Test
        @DisplayName("회장이 아닌 사람이 거절하면 예외가 발생한다")
        void rejectByNonOwner() {
            // given - 신청자를 회원으로 먼저 등록
            clubMemberShipRepository.save(ClubMembershipEntity.createMember(club, applicantProfile));

            // when & then
            assertThatThrownBy(() -> clubService.rejectClubApplication(
                    club.getId(), applicationForm.getId(), submission.getId(), applicantProfile, "거절 사유"))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getStatusCode()).isEqualTo(403);
                    });
        }

        @Test
        @DisplayName("이미 거절된 신청서를 다시 거절하면 예외가 발생한다")
        void rejectAlreadyRejected() {
            // given - 먼저 거절
            clubService.rejectClubApplication(club.getId(), applicationForm.getId(),
                    submission.getId(), ownerProfile, "첫 번째 거절");

            // when & then
            assertThatThrownBy(() -> clubService.rejectClubApplication(
                    club.getId(), applicationForm.getId(), submission.getId(), ownerProfile, "두 번째 거절"))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getStatusCode()).isEqualTo(409);
                    });
        }
    }
}
