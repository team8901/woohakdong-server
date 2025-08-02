package com.woohakdong.domain.util.application;

import com.woohakdong.domain.util.domain.AWSS3Service;
import com.woohakdong.domain.util.model.ImageResourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageApplicationService {

    private final AWSS3Service awsS3Service;

    public String generatePresignedUrls(ImageResourceType imageResourceType) {
        return awsS3Service.generatePresignedUrls(imageResourceType);
    }
}
