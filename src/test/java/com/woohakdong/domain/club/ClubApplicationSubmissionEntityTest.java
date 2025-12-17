package com.woohakdong.domain.club;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woohakdong.domain.club.model.ClubApplicationStatus;
import com.woohakdong.domain.club.model.ClubApplicationSubmissionEntity;
import com.woohakdong.exception.CustomException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ClubApplicationSubmissionEntityTest {

    @Nested
    @DisplayName("approve() 메서드 테스트")
    class ApproveTest {

        @Test
        @DisplayName("PENDING 상태에서 승인하면 APPROVED로 변경된다")
        void approveFromPending() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.PENDING);

            // when
            submission.approve();

            // then
            assertThat(submission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.APPROVED);
            assertThat(submission.getProcessedAt()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("이미 APPROVED 상태에서 승인하면 예외가 발생한다")
        void approveFromApproved() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.APPROVED);

            // when & then
            assertThatThrownBy(submission::approve)
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getCode()).isEqualTo("409008");
                    });
        }

        @Test
        @DisplayName("이미 REJECTED 상태에서 승인하면 예외가 발생한다")
        void approveFromRejected() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.REJECTED);

            // when & then
            assertThatThrownBy(submission::approve)
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getCode()).isEqualTo("409008");
                    });
        }
    }

    @Nested
    @DisplayName("reject() 메서드 테스트")
    class RejectTest {

        @Test
        @DisplayName("PENDING 상태에서 거절하면 REJECTED로 변경된다")
        void rejectFromPending() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.PENDING);
            String rejectionReason = "서류 미비";

            // when
            submission.reject(rejectionReason);

            // then
            assertThat(submission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.REJECTED);
            assertThat(submission.getRejectionReason()).isEqualTo(rejectionReason);
            assertThat(submission.getProcessedAt()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("거절 사유 없이 거절해도 정상 처리된다")
        void rejectWithoutReason() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.PENDING);

            // when
            submission.reject(null);

            // then
            assertThat(submission.getApplicationStatus()).isEqualTo(ClubApplicationStatus.REJECTED);
            assertThat(submission.getRejectionReason()).isNull();
            assertThat(submission.getProcessedAt()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("이미 APPROVED 상태에서 거절하면 예외가 발생한다")
        void rejectFromApproved() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.APPROVED);

            // when & then
            assertThatThrownBy(() -> submission.reject("거절 사유"))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getCode()).isEqualTo("409008");
                    });
        }

        @Test
        @DisplayName("이미 REJECTED 상태에서 다시 거절하면 예외가 발생한다")
        void rejectFromRejected() {
            // given
            ClubApplicationSubmissionEntity submission = createSubmissionWithStatus(ClubApplicationStatus.REJECTED);

            // when & then
            assertThatThrownBy(() -> submission.reject("거절 사유"))
                    .isInstanceOf(CustomException.class)
                    .satisfies(e -> {
                        CustomException ex = (CustomException) e;
                        assertThat(ex.getCode()).isEqualTo("409008");
                    });
        }
    }

    private ClubApplicationSubmissionEntity createSubmissionWithStatus(ClubApplicationStatus status) {
        return new ClubApplicationSubmissionEntity(
                1L, null, null, null, LocalDate.now(), status, null, null
        );
    }
}
