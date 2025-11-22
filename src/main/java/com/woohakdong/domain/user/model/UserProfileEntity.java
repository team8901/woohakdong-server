package com.woohakdong.domain.user.model;


import com.woohakdong.domain.auth.model.UserAuthEntity;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "user_profile")
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "major")
    private String major;

    @Column(name = "gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_auth_id")
    private UserAuthEntity userAuthEntity;

    public static UserProfileEntity createNewUser(UserAuthEntity userAuth, UserProfileCreateCommand command) {
        return new UserProfileEntity(
                null,
                userAuth.getName(),
                command.nickname(),
                userAuth.getEmail(),
                command.phoneNumber(),
                command.studentId(),
                command.major(),
                command.gender(),
                userAuth
        );
    }
}