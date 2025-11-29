package com.woohakdong.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.firebase.auth.FirebaseAuth;
import com.woohakdong.domain.auth.infrastructure.storage.repository.UserAuthRepository;
import com.woohakdong.domain.auth.model.UserAuthEntity;
import com.woohakdong.domain.auth.model.UserAuthRole;
import com.woohakdong.domain.club.infrastructure.storage.ClubMemberShipRepository;
import com.woohakdong.domain.club.infrastructure.storage.ClubRepository;
import com.woohakdong.domain.club.model.ClubEntity;
import com.woohakdong.domain.club.model.ClubMemberRole;
import com.woohakdong.domain.club.model.ClubMembershipEntity;
import com.woohakdong.domain.club.model.ClubRegisterCommand;
import com.woohakdong.domain.clubitem.domain.ClubItemService;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemHistoryRepository;
import com.woohakdong.domain.clubitem.infrastructure.storage.repository.ClubItemRepository;
import com.woohakdong.domain.clubitem.model.ClubItemCategory;
import com.woohakdong.domain.clubitem.model.ClubItemEntity;
import com.woohakdong.domain.clubitem.model.ClubItemHistoryEntity;
import com.woohakdong.domain.user.infrastructure.storage.repository.UserProfileRepository;
import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
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
class ClubItemRentalIntegrationTest {

    @MockitoBean
    private FirebaseAuth firebaseAuth;

    @Autowired
    private ClubItemService clubItemService;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubItemRepository clubItemRepository;

    @Autowired
    private ClubItemHistoryRepository clubItemHistoryRepository;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ClubMemberShipRepository clubMemberShipRepository;

    private ClubEntity club;
    private ClubMembershipEntity membership;
    private ClubItemEntity item;

    @BeforeEach
    void setUp() {
        clubItemHistoryRepository.deleteAll();
        clubItemRepository.deleteAll();
        clubMemberShipRepository.deleteAll();
        userProfileRepository.deleteAll();
        userAuthRepository.deleteAll();
        clubRepository.deleteAll();

        // 동아리 생성
        ClubRegisterCommand command = new ClubRegisterCommand(
                "테스트동아리", "testclub", "테스트", null, null, null, null, null, 10000
        );
        club = clubRepository.save(ClubEntity.create(command, LocalDate.now()));

        // 사용자 생성
        UserAuthEntity userAuth = userAuthRepository.save(new UserAuthEntity(
                null, "테스트유저", "test@test.com", UserAuthRole.USER, "google", "google-123"
        ));

        UserProfileEntity userProfile = userProfileRepository.save(new UserProfileEntity(
                null, "테스트유저", "테스터", "test@test.com", "010-1234-5678",
                "20231234", "컴퓨터공학과", Gender.MALE, userAuth
        ));

        // 동아리 멤버십 생성
        membership = clubMemberShipRepository.save(new ClubMembershipEntity(
                null, LocalDate.now(), ClubMemberRole.MEMBER, userProfile, "테스터", club
        ));

        // 물품 생성
        item = clubItemRepository.save(ClubItemEntity.create(
                club, "테스트 물품", null, "테스트용", "창고", ClubItemCategory.ETC, 7
        ));
    }

    @Test
    @DisplayName("물품 대여 성공")
    void rentClubItem_success() {
        // when
        ClubItemHistoryEntity history = clubItemService.rentClubItem(
                club.getId(), item.getId(), membership, 3
        );

        // then
        assertThat(history.getId()).isNotNull();
        assertThat(history.getRentalDate()).isEqualTo(LocalDate.now());
        assertThat(history.getDueDate()).isEqualTo(LocalDate.now().plusDays(3));
        assertThat(history.getReturnDate()).isNull();

        // 물품 상태 확인
        ClubItemEntity updatedItem = clubItemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getUsing()).isTrue();
    }

    @Test
    @DisplayName("물품 반납 성공")
    void returnClubItem_success() {
        // given: 물품 대여
        clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3);

        // when: 물품 반납
        clubItemService.returnClubItem(club.getId(), item.getId(), "https://example.com/return.jpg");

        // then
        ClubItemEntity updatedItem = clubItemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getUsing()).isFalse();

        ClubItemHistoryEntity history = clubItemHistoryRepository.findAll().get(0);
        assertThat(history.getReturnDate()).isEqualTo(LocalDate.now());
        assertThat(history.getReturnImage()).isEqualTo("https://example.com/return.jpg");
    }

    @Test
    @DisplayName("대여 후 다시 대여 시도하면 실패")
    void rentClubItem_alreadyRented() {
        // given: 물품 대여
        clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3);

        // when & then
        assertThatThrownBy(() -> clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 대여 중");
    }

    @Test
    @DisplayName("최대 대여일수 초과 시 실패")
    void rentClubItem_exceedMaxRentalDays() {
        // when & then: 최대 7일인데 10일 대여 시도
        assertThatThrownBy(() -> clubItemService.rentClubItem(club.getId(), item.getId(), membership, 10))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("최대 대여 일수를 초과");
    }

    @Test
    @DisplayName("대여 중이 아닌 물품 반납 시 실패")
    void returnClubItem_notRented() {
        // when & then
        assertThatThrownBy(() -> clubItemService.returnClubItem(club.getId(), item.getId(), null))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("대여 중이 아닙니다");
    }

    @Test
    @DisplayName("대여 불가 물품 대여 시도 실패")
    void rentClubItem_notAvailable() {
        // given: 물품 대여 불가로 설정
        item.updateAvailability(false);
        clubItemRepository.save(item);

        // when & then
        assertThatThrownBy(() -> clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("대여할 수 없는 상태");
    }

    @Test
    @DisplayName("대여 후 반납 후 다시 대여 가능")
    void rentReturnAndRentAgain() {
        // given: 대여 후 반납
        clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3);
        clubItemService.returnClubItem(club.getId(), item.getId(), null);

        // when: 다시 대여
        ClubItemHistoryEntity newHistory = clubItemService.rentClubItem(
                club.getId(), item.getId(), membership, 5
        );

        // then
        assertThat(newHistory.getDueDate()).isEqualTo(LocalDate.now().plusDays(5));
        assertThat(clubItemHistoryRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("대여 내역 조회")
    void getClubItemHistory() {
        // given: 대여
        clubItemService.rentClubItem(club.getId(), item.getId(), membership, 3);

        // when
        var histories = clubItemService.getClubItemHistory(club.getId());

        // then
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getClubItem().getName()).isEqualTo("테스트 물품");
        assertThat(histories.get(0).getClubMembership().getNickname()).isEqualTo("테스터");
    }
}
