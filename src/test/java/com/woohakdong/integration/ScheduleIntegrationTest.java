package com.woohakdong.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.firebase.auth.FirebaseAuth;
import com.woohakdong.api.dto.request.ScheduleCreateRequest;
import com.woohakdong.api.dto.request.ScheduleUpdateRequest;
import com.woohakdong.api.dto.response.ListWrapper;
import com.woohakdong.api.dto.response.ScheduleIdResponse;
import com.woohakdong.api.dto.response.ScheduleResponse;
import com.woohakdong.api.facade.ScheduleFacade;
import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMemberRole;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.schedule.infrastructure.storage.ScheduleRepository;
import com.woohakdong.domain.user.infrastructure.storage.repository.UserProfileRepository;
import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class ScheduleIntegrationTest {

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ScheduleFacade scheduleFacade;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ClubMemberShipRepository clubMemberShipRepository;

    private Long clubId;
    private Long presidentAuthId;
    private Long memberAuthId;

    @BeforeEach
    void setUp() {
        scheduleRepository.deleteAll();
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

        // 회장 생성
        UserAuthEntity presidentAuth = userAuthRepository.save(new UserAuthEntity(
                null, "회장유저", "president@test.com", UserAuthRole.USER, "google", "google-president"
        ));
        presidentAuthId = presidentAuth.getId();

        UserProfileEntity presidentProfile = userProfileRepository.save(new UserProfileEntity(
                null, "회장유저", "회장닉네임", "president@test.com", "010-1111-1111",
                "20231111", "컴퓨터공학과", Gender.MALE, presidentAuth
        ));

        clubMemberShipRepository.save(new ClubMembershipEntity(
                null, LocalDate.now(), ClubMemberRole.PRESIDENT, presidentProfile, "회장닉네임", savedClub
        ));

        // 일반 회원 생성
        UserAuthEntity memberAuth = userAuthRepository.save(new UserAuthEntity(
                null, "일반유저", "member@test.com", UserAuthRole.USER, "google", "google-member"
        ));
        memberAuthId = memberAuth.getId();

        UserProfileEntity memberProfile = userProfileRepository.save(new UserProfileEntity(
                null, "일반유저", "일반닉네임", "member@test.com", "010-2222-2222",
                "20232222", "컴퓨터공학과", Gender.FEMALE, memberAuth
        ));

        clubMemberShipRepository.save(new ClubMembershipEntity(
                null, LocalDate.now(), ClubMemberRole.MEMBER, memberProfile, "일반닉네임", savedClub
        ));
    }

    @Test
    @DisplayName("일정 등록 후 조회 - 전체 플로우 테스트")
    void createAndGetSchedule() {
        // given
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                "정기 스터디",
                "매주 화요일 정기 스터디입니다.",
                "#6366f1",
                LocalDateTime.of(2025, 7, 24, 14, 0),
                LocalDateTime.of(2025, 7, 24, 16, 0)
        );

        // when: 일정 등록 (회장)
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId, request);

        // then
        assertThat(createResponse.scheduleId()).isNotNull();

        // when: 일정 목록 조회
        ListWrapper<ScheduleResponse> listResponse = scheduleFacade.getSchedules(clubId);

        // then
        assertThat(listResponse.data()).hasSize(1);
        ScheduleResponse schedule = listResponse.data().get(0);
        assertThat(schedule.title()).isEqualTo("정기 스터디");
        assertThat(schedule.content()).isEqualTo("매주 화요일 정기 스터디입니다.");
        assertThat(schedule.color()).isEqualTo("#6366f1");
        assertThat(schedule.startTime()).isEqualTo(LocalDateTime.of(2025, 7, 24, 14, 0));
        assertThat(schedule.endTime()).isEqualTo(LocalDateTime.of(2025, 7, 24, 16, 0));
        assertThat(schedule.writer()).isEqualTo("회장닉네임");

        // when: 일정 단건 조회
        ScheduleResponse singleSchedule = scheduleFacade.getSchedule(clubId, createResponse.scheduleId());

        // then
        assertThat(singleSchedule.title()).isEqualTo("정기 스터디");
        assertThat(singleSchedule.startTime()).isEqualTo(LocalDateTime.of(2025, 7, 24, 14, 0));
    }

    @Test
    @DisplayName("여러 일정 등록 후 목록 조회 - 시작 일시 오름차순 정렬")
    void createMultipleSchedulesAndGetList() {
        // given: 여러 일정 등록 (날짜 순서 뒤섞어서)
        scheduleFacade.createSchedule(clubId, presidentAuthId, new ScheduleCreateRequest(
                "세 번째 일정", "내용3", null,
                LocalDateTime.of(2025, 7, 26, 10, 0),
                LocalDateTime.of(2025, 7, 26, 12, 0)
        ));
        scheduleFacade.createSchedule(clubId, presidentAuthId, new ScheduleCreateRequest(
                "첫 번째 일정", "내용1", null,
                LocalDateTime.of(2025, 7, 20, 10, 0),
                LocalDateTime.of(2025, 7, 20, 12, 0)
        ));
        scheduleFacade.createSchedule(clubId, presidentAuthId, new ScheduleCreateRequest(
                "두 번째 일정", "내용2", null,
                LocalDateTime.of(2025, 7, 24, 14, 0),
                LocalDateTime.of(2025, 7, 24, 16, 0)
        ));

        // when: 목록 조회
        ListWrapper<ScheduleResponse> listResponse = scheduleFacade.getSchedules(clubId);

        // then: 시작 일시 오름차순 (7/20 -> 7/24 -> 7/26)
        assertThat(listResponse.data()).hasSize(3);
        assertThat(listResponse.data().get(0).title()).isEqualTo("첫 번째 일정");
        assertThat(listResponse.data().get(1).title()).isEqualTo("두 번째 일정");
        assertThat(listResponse.data().get(2).title()).isEqualTo("세 번째 일정");
    }

    @Test
    @DisplayName("일정 수정 후 변경 반영 확인")
    void updateSchedule() {
        // given: 일정 등록
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId,
                new ScheduleCreateRequest(
                        "원래 제목", "원래 내용", "#ff0000",
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                ));

        // when: 일정 수정
        ScheduleUpdateRequest updateRequest = new ScheduleUpdateRequest(
                "수정된 제목",
                "수정된 내용",
                "#00ff00",
                LocalDateTime.of(2025, 8, 1, 10, 0),
                LocalDateTime.of(2025, 8, 1, 12, 0)
        );
        scheduleFacade.updateSchedule(clubId, createResponse.scheduleId(), presidentAuthId, updateRequest);

        // then: 수정 내용 확인
        ScheduleResponse updated = scheduleFacade.getSchedule(clubId, createResponse.scheduleId());
        assertThat(updated.title()).isEqualTo("수정된 제목");
        assertThat(updated.content()).isEqualTo("수정된 내용");
        assertThat(updated.color()).isEqualTo("#00ff00");
        assertThat(updated.startTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 10, 0));
        assertThat(updated.endTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 12, 0));
    }

    @Test
    @DisplayName("일정 삭제 (soft delete) 후 조회 불가 확인")
    void deleteSchedule() {
        // given: 일정 등록
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId,
                new ScheduleCreateRequest(
                        "삭제할 일정", "삭제될 내용", null,
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                ));

        // 등록 확인
        assertThat(scheduleFacade.getSchedules(clubId).data()).hasSize(1);

        // when: 일정 삭제
        scheduleFacade.deleteSchedule(clubId, createResponse.scheduleId(), presidentAuthId);

        // then: 조회 시 보이지 않음 (soft delete)
        assertThat(scheduleFacade.getSchedules(clubId).data()).isEmpty();
        assertThatThrownBy(() -> scheduleFacade.getSchedule(clubId, createResponse.scheduleId()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("해당 일정을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("일반 회원이 일정 등록 시 권한 예외 발생")
    void createScheduleByMemberForbidden() {
        assertThatThrownBy(() -> scheduleFacade.createSchedule(clubId, memberAuthId,
                new ScheduleCreateRequest(
                        "무단 일정", "권한 없음", null,
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                )))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("동아리 소유자만 접근할 수 있습니다");
    }

    @Test
    @DisplayName("일반 회원이 일정 수정 시 권한 예외 발생")
    void updateScheduleByMemberForbidden() {
        // given: 회장이 일정 등록
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId,
                new ScheduleCreateRequest(
                        "일정 제목", "일정 내용", null,
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                ));

        // when & then: 일반 회원이 수정 시 예외
        assertThatThrownBy(() -> scheduleFacade.updateSchedule(clubId, createResponse.scheduleId(), memberAuthId,
                new ScheduleUpdateRequest(
                        "수정 제목", "수정 내용", null,
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                )))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("동아리 소유자만 접근할 수 있습니다");
    }

    @Test
    @DisplayName("일반 회원이 일정 삭제 시 권한 예외 발생")
    void deleteScheduleByMemberForbidden() {
        // given: 회장이 일정 등록
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId,
                new ScheduleCreateRequest(
                        "일정 제목", "일정 내용", null,
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                ));

        // when & then: 일반 회원이 삭제 시 예외
        assertThatThrownBy(() -> scheduleFacade.deleteSchedule(clubId, createResponse.scheduleId(), memberAuthId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("동아리 소유자만 접근할 수 있습니다");
    }

    @Test
    @DisplayName("존재하지 않는 일정 조회 시 예외 발생")
    void getScheduleNotFound() {
        assertThatThrownBy(() -> scheduleFacade.getSchedule(clubId, 9999L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("해당 일정을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("color가 null인 일정 등록")
    void createScheduleWithNullColor() {
        // given
        ScheduleCreateRequest request = new ScheduleCreateRequest(
                "색상 없는 일정",
                "색상을 지정하지 않은 일정입니다.",
                null,
                LocalDateTime.of(2025, 7, 24, 14, 0),
                LocalDateTime.of(2025, 7, 24, 16, 0)
        );

        // when
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId, request);

        // then
        ScheduleResponse schedule = scheduleFacade.getSchedule(clubId, createResponse.scheduleId());
        assertThat(schedule.color()).isNull();
    }

    @Test
    @DisplayName("일정 등록 후 수정 후 삭제 - 전체 CRUD 플로우")
    void fullCrudFlow() {
        // 1. Create
        ScheduleIdResponse createResponse = scheduleFacade.createSchedule(clubId, presidentAuthId,
                new ScheduleCreateRequest(
                        "CRUD 테스트", "생성", "#ff0000",
                        LocalDateTime.of(2025, 7, 24, 14, 0),
                        LocalDateTime.of(2025, 7, 24, 16, 0)
                ));
        Long scheduleId = createResponse.scheduleId();
        assertThat(scheduleId).isNotNull();

        // 2. Read
        ScheduleResponse readResponse = scheduleFacade.getSchedule(clubId, scheduleId);
        assertThat(readResponse.title()).isEqualTo("CRUD 테스트");

        // 3. Update
        scheduleFacade.updateSchedule(clubId, scheduleId, presidentAuthId,
                new ScheduleUpdateRequest(
                        "CRUD 테스트 (수정됨)", "수정됨", "#00ff00",
                        LocalDateTime.of(2025, 8, 1, 10, 0),
                        LocalDateTime.of(2025, 8, 1, 12, 0)
                ));

        ScheduleResponse updatedResponse = scheduleFacade.getSchedule(clubId, scheduleId);
        assertThat(updatedResponse.title()).isEqualTo("CRUD 테스트 (수정됨)");
        assertThat(updatedResponse.color()).isEqualTo("#00ff00");

        // 4. Delete
        scheduleFacade.deleteSchedule(clubId, scheduleId, presidentAuthId);
        assertThat(scheduleFacade.getSchedules(clubId).data()).isEmpty();
    }
}
