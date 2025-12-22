package com.woohakdong.api.dto.request;

import com.woohakdong.domain.activity.model.ActivityCreateCommand;
import com.woohakdong.domain.activity.model.ActivityTag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ActivityCreateRequest(

        @Schema(example = "코테 스터디", description = "활동 제목")
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @Schema(example = "오늘 스터디 하이라이트...", description = "활동 내용")
        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @Schema(example = "4", description = "참가자 수, 미입력시 null로 저장")
        Integer participantCount,

        @Schema(example = "2025-07-24", description = "활동 날짜")
        @NotNull(message = "활동 날짜는 필수입니다.")
        LocalDate activityDate,

        @Schema(example = "STUDY", description = "활동 유형 (STUDY, PARTY, MEETING, MT, ETC)")
        @NotNull(message = "활동 유형은 필수입니다.")
        ActivityTag tag,

        @Schema(description = "활동 이미지 URL 목록 (최대 5개)")
        List<String> activityImages
) {
    public ActivityCreateCommand toCommand() {
        return new ActivityCreateCommand(
                title,
                content,
                participantCount,
                activityDate,
                tag,
                activityImages != null ? activityImages : List.of()
        );
    }
}
