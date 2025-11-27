package com.woohakdong.domain.clubitem.model;

import com.woohakdong.domain.club.model.ClubMembershipEntity;
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
@Table(name = "club_item_history")
public class ClubItemHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_item_id", nullable = false)
    private ClubItemEntity clubItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_membership_id", nullable = false)
    private ClubMembershipEntity clubMembership;

    @Column(name = "rental_date", nullable = false)
    private LocalDate rentalDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "return_image")
    private String returnImage;

    public static ClubItemHistoryEntity create(
            ClubItemEntity clubItem,
            ClubMembershipEntity clubMembership,
            LocalDate rentalDate,
            LocalDate dueDate
    ) {
        return new ClubItemHistoryEntity(
                null,
                clubItem,
                clubMembership,
                rentalDate,
                dueDate,
                null,
                null
        );
    }

    public void returnItem(LocalDate returnDate, String returnImage) {
        this.returnDate = returnDate;
        this.returnImage = returnImage;
    }

    public boolean isOverdue() {
        if (returnDate != null) {
            return false; // 이미 반납됨
        }
        return LocalDate.now().isAfter(dueDate);
    }
}
