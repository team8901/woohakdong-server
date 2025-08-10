package com.woohakdong.domain.club.model;

public enum ClubApplicationStatus {
    PENDING, // 신청 대기
    APPROVED, // 승인됨
    REJECTED, // 거절됨
    WITHDRAWN; // 신청 철회됨

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isApproved() {
        return this == APPROVED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }

    public boolean isWithdrawn() {
        return this == WITHDRAWN;
    }
}
