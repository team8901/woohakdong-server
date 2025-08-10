package com.woohakdong.api.facade;

import com.woohakdong.api.dto.response.PresignedUrlResponse;
import com.woohakdong.domain.util.application.ImageService;
import com.woohakdong.domain.util.model.ImageResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilFacade {

    private final ImageService imageService;

    public PresignedUrlResponse getPresignedUrls(ImageResourceType imageResourceType) {
        return PresignedUrlResponse.of(imageService.generatePresignedUrls(imageResourceType));
    }
}
