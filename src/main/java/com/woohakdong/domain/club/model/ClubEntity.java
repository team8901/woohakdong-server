package com.woohakdong.domain.club.model;

import com.woohakdong.domain.user.model.UserProfileEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "club")
public class ClubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nameEn;
    private String description;
    private String thumbnailImageUrl;
    private String bannerImageUrl;
    private String roomInfo; // 과방
    private String groupChatLink; // 채팅방 링크
    private String groupChatPassword; // 채팅방 비밀번호
    private Integer dues; // 회비

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserProfileEntity owner; // 소유자

    private LocalDate subscriptionStartDate; // 구독 시작일
    private LocalDate subscriptionExpireDate; // 구독 종료일

    public static ClubEntity create(ClubRegisterCommand command, LocalDate now) {
        return new ClubEntity(
                null,
                command.name(),
                command.nameEn(),
                command.description(),
                command.thumbnailImageUrl(),
                command.bannerImageUrl(),
                command.roomInfo(),
                command.groupChatLink(),
                command.groupChatPassword(),
                command.dues(),
                null,
                now,
                now.plusMonths(6) // 구독 종료일은 시작일로부터 1개월 후로 설정
        );
    }

    public void updateOwner(UserProfileEntity userProfile) {
        this.owner = userProfile;
    }
}
