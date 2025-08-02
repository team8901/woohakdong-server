package com.woohakdong.controller.dto.response;

public record PresignedUrlResponse(
        String presignedUrl
) {
    public static PresignedUrlResponse of(String presignedUrl) {
        return new PresignedUrlResponse(presignedUrl);
    }
}
