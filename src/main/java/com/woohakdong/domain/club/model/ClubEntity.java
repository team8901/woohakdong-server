package com.woohakdong.domain.club.model;

import com.woohakdong.domain.user.model.UserProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail_image_url")
    private String thumbnailImageUrl;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

    @Column(name = "room_info")
    private String roomInfo; // 과방

    @Column(name = "group_chat_link")
    private String groupChatLink; // 채팅방 링크

    @Column(name = "group_chat_password")
    private String groupChatPassword; // 채팅방 비밀번호

    @Column(name = "dues")
    private Integer dues; // 회비

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_profile_id")
    private UserProfileEntity owner; // 소유자

    @Column(name = "subscription_start_date", nullable = false)
    private LocalDate subscriptionStartDate; // 구독 시작일

    @Column(name = "subscription_expire_date")
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
