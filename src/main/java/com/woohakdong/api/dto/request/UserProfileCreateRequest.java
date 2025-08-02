package com.woohakdong.api.dto.request;

import com.woohakdong.domain.user.model.Gender;
import com.woohakdong.domain.user.model.UserProfileCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserProfileCreateRequest(

        @Schema(example = "준상박", description = "사용자의 닉네임")
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(example = "010-1234-5678", description = "사용자의 전화번호")
        @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다. 예: 010-1234-5678")
        String phoneNumber,

        @Schema(example = "2023123456", description = "사용자의 학번")
        @NotBlank(message = "학번은 필수입니다.")
        String studentId,

        @Schema(example = "MALE", description = "사용자의 성별")
        @NotNull(message = "성별은 필수입니다.")
        Gender gender
) {
    public UserProfileCreateCommand toCommandModel() {
        return new UserProfileCreateCommand(nickname, phoneNumber, studentId, gender);
    }

}

