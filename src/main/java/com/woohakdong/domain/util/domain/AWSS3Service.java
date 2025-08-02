package com.woohakdong.domain.util.domain;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.woohakdong.domain.util.model.ImageResourceType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AWSS3Service {

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String S3_BUCKET_NAME;
    private final AmazonS3 amazonS3;

    private final static String IMAGE_FILE_PNG_EXTENSION = ".png";

    public String generatePresignedUrls(ImageResourceType imageResourceType) {
        String key = createKey(imageResourceType);
        Date expirationDate = getExpirationTime();
        GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET_NAME, key);
        presignedUrlRequest.setMethod(HttpMethod.PUT);
        presignedUrlRequest.setExpiration(expirationDate);
        return amazonS3.generatePresignedUrl(presignedUrlRequest).toExternalForm();
    }

    private Date getExpirationTime() {
        Instant expirationInstant = Instant.now().plus(Duration.ofMinutes(3));
        return Date.from(expirationInstant);
    }

    private String createKey(ImageResourceType imageResourceType) {
        return imageResourceType.getValue() + "/" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/" +
                RandomStringUtils.secure().nextAlphabetic(5) +
                IMAGE_FILE_PNG_EXTENSION;
    }
}
