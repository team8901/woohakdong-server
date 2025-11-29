package com.woohakdong.domain.clubitem.model;

import com.woohakdong.domain.club.model.ClubEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "club_item")
public class ClubItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private ClubEntity club;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "photo")
    private String photo;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ClubItemCategory category;

    @Column(name = "rental_max_day", nullable = false)
    private Integer rentalMaxDay;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "using_flag", nullable = false)
    private Boolean using;

    @Column(name = "rental_date")
    private LocalDate rentalDate;

    @Column(name = "rental_time")
    private Integer rentalTime;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public static ClubItemEntity create(
            ClubEntity club,
            String name,
            String photo,
            String description,
            String location,
            ClubItemCategory category,
            Integer rentalMaxDay
    ) {
        return new ClubItemEntity(
                null,
                club,
                name,
                photo,
                description,
                location,
                category,
                rentalMaxDay,
                true,   // 기본값: 대여 가능
                false,  // 기본값: 사용 중 아님
                null,
                0,
                false   // 기본값: 삭제되지 않음
        );
    }

    public void update(ClubItemUpdateCommand command) {
        this.name = command.name();
        this.photo = command.photo();
        this.description = command.description();
        this.location = command.location();
        this.category = command.category();
        this.rentalMaxDay = command.rentalMaxDay();
        this.available = command.available();
    }

    public void delete() {
        this.deleted = true;
    }

    public void rent(LocalDate rentalDate, Integer rentalTime) {
        this.using = true;
        this.rentalDate = rentalDate;
        this.rentalTime = rentalTime;
    }

    public void returnItem() {
        this.using = false;
        this.rentalDate = null;
        this.rentalTime = 0;
    }

    public void updateAvailability(Boolean available) {
        this.available = available;
    }
}
