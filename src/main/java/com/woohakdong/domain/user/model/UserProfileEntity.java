package com.woohakdong.domain.user.model;


import com.woohakdong.domain.auth.model.UserAuthEntity;
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
    private String name;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String studentId;

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
                command.gender(),
                userAuth
        );
    }
}