package com.woohakdong.api.dto.response;

public record PresignedUrlResponse(
        String presignedUrl
) {
    public static PresignedUrlResponse of(String presignedUrl) {
        return new PresignedUrlResponse(presignedUrl);
    }
}
