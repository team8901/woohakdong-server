package com.woohakdong.controller.dto.request;

import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileCreateCommand;
import jakarta.validation.constraints.AssertTrue;

public record UserProfileCreateRequest(
        String nickname,
        String phoneNumber,
        String studentId,
        Gender gender
) {
    public UserProfileCreateCommand toCommandModel() {
        return new UserProfileCreateCommand(nickname, phoneNumber, studentId, gender);
    }

    public UserProfileCreateRequest {
        if (gender == null) {
            gender = Gender.UNDISCLOSED;
        }
    }

    @AssertTrue(message = "nickname은 반드시 입력되어야합니다.")
    private boolean _isNickNameValid() {
        return nickname != null && !nickname.isBlank();
    }

    @AssertTrue(message = "phoneNumber는 반드시 입력되어야합니다.")
    private boolean _isPhoneNumberValid() {
        return phoneNumber != null && !phoneNumber.isBlank();
    }

    @AssertTrue(message = "studentId는 반드시 입력되어야합니다.")
    private boolean _isStudentIdValid() {
        return studentId != null && !studentId.isBlank();
    }
}

