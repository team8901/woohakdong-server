package com.woohakdong.api.dto.request;

import com.woohakdong.domain.schedule.model.ScheduleUpdateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleUpdateRequest(

        @Schema(example = "정기 스터디", description = "일정 제목")
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @Schema(example = "매주 화요일 정기 스터디 모임입니다.", description = "일정 내용")
        @NotBlank(message = "내용은 필수입니다.")
        String content,

        @Schema(example = "#6366f1", description = "캘린더 표시 색상 (hex code, 미입력 시 프론트 기본값 적용)")
        String color,

        @Schema(example = "2025-07-24T14:00:00", description = "시작 일시")
        @NotNull(message = "시작 일시는 필수입니다.")
        LocalDateTime startTime,

        @Schema(example = "2025-07-24T16:00:00", description = "종료 일시")
        @NotNull(message = "종료 일시는 필수입니다.")
        LocalDateTime endTime
) {
    public ScheduleUpdateCommand toCommand() {
        return new ScheduleUpdateCommand(
                title,
                content,
                color,
                startTime,
                endTime
        );
    }
}
