package com.woohakdong.api.facade;

import com.woohakdong.api.dto.response.PresignedUrlResponse;
import com.woohakdong.domain.util.application.ImageApplicationService;
import com.woohakdong.domain.util.model.ImageResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilFacade {

    private final ImageApplicationService imageApplicationService;

    public PresignedUrlResponse getPresignedUrls(ImageResourceType imageResourceType) {
        return PresignedUrlResponse.of(imageApplicationService.generatePresignedUrls(imageResourceType));
    }
}
