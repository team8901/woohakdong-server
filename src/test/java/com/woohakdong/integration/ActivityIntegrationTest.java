package com.woohakdong.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.firebase.auth.FirebaseAuth;
import com.woohakdong.api.dto.request.ActivityCreateRequest;
import com.woohakdong.api.dto.request.ActivityUpdateRequest;
import com.woohakdong.api.dto.response.ActivityIdResponse;
import com.woohakdong.api.dto.response.ActivityResponse;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.facade.ActivityFacade;
import com.woohakdong.domain.activity.infrastructure.storage.ActivityRepository;
import com.woohakdong.domain.activity.model.ActivityTag;
import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ActivityIntegrationTest {

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ActivityFacade activityFacade;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ClubMemberShipRepository clubMemberShipRepository;

    private Long clubId;
    private Long userAuthId;
    private ClubMembershipEntity membership;

    @BeforeEach
    void setUp() {
        activityRepository.deleteAll();
        clubMemberShipRepository.deleteAll();
        userProfileRepository.deleteAll();
        userAuthRepository.deleteAll();
        clubRepository.deleteAll();

        // 동아리 생성
        ClubRegisterCommand command = new ClubRegisterCommand(
                "테스트동아리",
                "testclub",
                "테스트 동아리입니다",
                null,
                null,
                "팔달관 101호",
                null,
                null,
                10000
        );
        ClubEntity club = ClubEntity.create(command, LocalDate.now());
        ClubEntity savedClub = clubRepository.save(club);
        clubId = savedClub.getId();

        // 사용자 생성
        UserAuthEntity userAuth = userAuthRepository.save(new UserAuthEntity(
                null, "테스트유저", "test@test.com", UserAuthRole.USER, "google", "google-123"
        ));
        userAuthId = userAuth.getId();

        UserProfileEntity userProfile = userProfileRepository.save(new UserProfileEntity(
                null, "테스트유저", "테스터", "test@test.com", "010-1234-5678",
                "20231234", "컴퓨터공학과", Gender.MALE, userAuth
        ));

        // 동아리 멤버십 생성
        membership = clubMemberShipRepository.save(new ClubMembershipEntity(
                null, LocalDate.now(), ClubMemberRole.MEMBER, userProfile, "테스터", savedClub
        ));
    }

    @Test
    @DisplayName("활동 등록 후 조회 - 전체 플로우 테스트")
    void createAndGetActivity() {
        // given
        ActivityCreateRequest request = new ActivityCreateRequest(
                "코테 스터디",
                "오늘 스터디 하이라이트입니다.",
                4,
                LocalDate.of(2025, 7, 24),
                ActivityTag.STUDY,
                List.of("https://example.com/image1.jpg")
        );

        // when: 활동 등록
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId, request);

        // then
        assertThat(createResponse.activityId()).isNotNull();

        // when: 활동 목록 조회
        ListWrapper<ActivityResponse> listResponse = activityFacade.getActivities(clubId);

        // then
        assertThat(listResponse.data()).hasSize(1);
        ActivityResponse activity = listResponse.data().get(0);
        assertThat(activity.title()).isEqualTo("코테 스터디");
        assertThat(activity.content()).isEqualTo("오늘 스터디 하이라이트입니다.");
        assertThat(activity.participantCount()).isEqualTo(4);
        assertThat(activity.tag()).isEqualTo(ActivityTag.STUDY);
        assertThat(activity.writer()).isEqualTo("테스터");
        assertThat(activity.activityImages()).containsExactly("https://example.com/image1.jpg");

        // when: 활동 단건 조회
        ActivityResponse singleActivity = activityFacade.getActivity(clubId, createResponse.activityId());

        // then
        assertThat(singleActivity.title()).isEqualTo("코테 스터디");
        assertThat(singleActivity.tag()).isEqualTo(ActivityTag.STUDY);
    }

    @Test
    @DisplayName("여러 활동 등록 후 목록 조회 - 활동 날짜 내림차순 정렬")
    void createMultipleActivitiesAndGetList() {
        // given: 여러 활동 등록 (날짜 순서대로)
        activityFacade.createActivity(clubId, userAuthId, new ActivityCreateRequest(
                "첫 번째 활동", "내용1", 3, LocalDate.of(2025, 7, 20),
                ActivityTag.STUDY, null
        ));
        activityFacade.createActivity(clubId, userAuthId, new ActivityCreateRequest(
                "두 번째 활동", "내용2", 5, LocalDate.of(2025, 7, 25),
                ActivityTag.MEETING, null
        ));
        activityFacade.createActivity(clubId, userAuthId, new ActivityCreateRequest(
                "세 번째 활동", "내용3", 10, LocalDate.of(2025, 7, 22),
                ActivityTag.MT, null
        ));

        // when: 목록 조회
        ListWrapper<ActivityResponse> listResponse = activityFacade.getActivities(clubId);

        // then: 활동 날짜 내림차순 (7/25 -> 7/22 -> 7/20)
        assertThat(listResponse.data()).hasSize(3);
        assertThat(listResponse.data().get(0).title()).isEqualTo("두 번째 활동");
        assertThat(listResponse.data().get(1).title()).isEqualTo("세 번째 활동");
        assertThat(listResponse.data().get(2).title()).isEqualTo("첫 번째 활동");
    }

    @Test
    @DisplayName("활동 수정")
    void updateActivity() {
        // given: 활동 등록
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId,
                new ActivityCreateRequest(
                        "원래 제목", "원래 내용", 4, LocalDate.of(2025, 7, 24),
                        ActivityTag.STUDY, null
                ));

        // when: 활동 수정
        ActivityUpdateRequest updateRequest = new ActivityUpdateRequest(
                "수정된 제목",
                "수정된 내용",
                8,
                LocalDate.of(2025, 8, 1),
                ActivityTag.PARTY,
                List.of("https://example.com/new-image.jpg")
        );
        activityFacade.updateActivity(clubId, createResponse.activityId(), userAuthId, updateRequest);

        // then: 수정 내용 확인
        ActivityResponse updated = activityFacade.getActivity(clubId, createResponse.activityId());
        assertThat(updated.title()).isEqualTo("수정된 제목");
        assertThat(updated.content()).isEqualTo("수정된 내용");
        assertThat(updated.participantCount()).isEqualTo(8);
        assertThat(updated.tag()).isEqualTo(ActivityTag.PARTY);
        assertThat(updated.activityImages()).containsExactly("https://example.com/new-image.jpg");
    }

    @Test
    @DisplayName("활동 삭제 (soft delete)")
    void deleteActivity() {
        // given: 활동 등록
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId,
                new ActivityCreateRequest(
                        "삭제할 활동", "삭제될 내용", 5, LocalDate.of(2025, 7, 24),
                        ActivityTag.ETC, null
                ));

        // 등록 확인
        assertThat(activityFacade.getActivities(clubId).data()).hasSize(1);

        // when: 활동 삭제
        activityFacade.deleteActivity(clubId, createResponse.activityId(), userAuthId);

        // then: 조회 시 보이지 않음 (soft delete)
        assertThat(activityFacade.getActivities(clubId).data()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 활동 조회 시 예외 발생")
    void getActivityNotFound() {
        assertThatThrownBy(() -> activityFacade.getActivity(clubId, 9999L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("활동 기록을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 활동 수정 시 예외 발생")
    void updateActivityNotFound() {
        ActivityUpdateRequest updateRequest = new ActivityUpdateRequest(
                "수정 제목", "수정 내용", 5, LocalDate.of(2025, 7, 24),
                ActivityTag.STUDY, null
        );

        assertThatThrownBy(() -> activityFacade.updateActivity(clubId, 9999L, userAuthId, updateRequest))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("활동 기록을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 활동 삭제 시 예외 발생")
    void deleteActivityNotFound() {
        assertThatThrownBy(() -> activityFacade.deleteActivity(clubId, 9999L, userAuthId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("활동 기록을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("다양한 태그로 활동 등록")
    void createActivitiesWithDifferentTags() {
        // given & when: 각 태그별 활동 등록
        for (ActivityTag tag : ActivityTag.values()) {
            activityFacade.createActivity(clubId, userAuthId, new ActivityCreateRequest(
                    tag.name() + " 활동",
                    tag.name() + " 내용",
                    5,
                    LocalDate.of(2025, 7, 24),
                    tag,
                    null
            ));
        }

        // then: 모든 태그의 활동이 등록됨
        ListWrapper<ActivityResponse> listResponse = activityFacade.getActivities(clubId);
        assertThat(listResponse.data()).hasSize(ActivityTag.values().length);
    }

    @Test
    @DisplayName("여러 이미지로 활동 등록")
    void createActivityWithMultipleImages() {
        // given
        ActivityCreateRequest request = new ActivityCreateRequest(
                "이미지 많은 활동",
                "여러 이미지가 포함된 활동입니다.",
                10,
                LocalDate.of(2025, 8, 1),
                ActivityTag.MT,
                List.of(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg",
                        "https://example.com/image3.jpg"
                )
        );

        // when
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId, request);

        // then
        ActivityResponse activity = activityFacade.getActivity(clubId, createResponse.activityId());
        assertThat(activity.activityImages()).hasSize(3);
        assertThat(activity.activityImages()).containsExactly(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
        );
    }

    @Test
    @DisplayName("participantCount가 null인 활동 등록")
    void createActivityWithNullParticipantCount() {
        // given
        ActivityCreateRequest request = new ActivityCreateRequest(
                "참가자 수 미입력 활동",
                "참가자 수를 입력하지 않은 활동입니다.",
                null,
                LocalDate.of(2025, 7, 24),
                ActivityTag.ETC,
                null
        );

        // when
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId, request);

        // then
        ActivityResponse activity = activityFacade.getActivity(clubId, createResponse.activityId());
        assertThat(activity.participantCount()).isNull();
    }

    @Test
    @DisplayName("이미지 없이 활동 등록")
    void createActivityWithoutImages() {
        // given
        ActivityCreateRequest request = new ActivityCreateRequest(
                "이미지 없는 활동",
                "이미지가 없는 활동입니다.",
                5,
                LocalDate.of(2025, 7, 24),
                ActivityTag.MEETING,
                null
        );

        // when
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId, request);

        // then
        ActivityResponse activity = activityFacade.getActivity(clubId, createResponse.activityId());
        assertThat(activity.activityImages()).isEmpty();
    }

    @Test
    @DisplayName("활동 등록 후 수정 후 삭제 - 전체 CRUD 플로우")
    void fullCrudFlow() {
        // 1. Create
        ActivityIdResponse createResponse = activityFacade.createActivity(clubId, userAuthId,
                new ActivityCreateRequest(
                        "CRUD 테스트", "생성", 1, LocalDate.of(2025, 7, 24),
                        ActivityTag.STUDY, null
                ));
        Long activityId = createResponse.activityId();
        assertThat(activityId).isNotNull();

        // 2. Read
        ActivityResponse readResponse = activityFacade.getActivity(clubId, activityId);
        assertThat(readResponse.title()).isEqualTo("CRUD 테스트");

        // 3. Update
        activityFacade.updateActivity(clubId, activityId, userAuthId,
                new ActivityUpdateRequest(
                        "CRUD 테스트 (수정됨)", "수정됨", 2, LocalDate.of(2025, 7, 25),
                        ActivityTag.PARTY, List.of("https://example.com/updated.jpg")
                ));

        ActivityResponse updatedResponse = activityFacade.getActivity(clubId, activityId);
        assertThat(updatedResponse.title()).isEqualTo("CRUD 테스트 (수정됨)");
        assertThat(updatedResponse.tag()).isEqualTo(ActivityTag.PARTY);

        // 4. Delete
        activityFacade.deleteActivity(clubId, activityId, userAuthId);
        assertThat(activityFacade.getActivities(clubId).data()).isEmpty();
    }
}
