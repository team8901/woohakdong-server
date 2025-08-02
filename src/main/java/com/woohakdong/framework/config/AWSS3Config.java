package com.woohakdong.framework.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * AWS S3 설정 클래스
 * <p>
 * AWS_ACCESS_KEY_ID와 AWS_SECRET_ACCESS_KEY의 값을 환경 변수나
 * <p>
 * aws configure 명령어를 통해서 주입하여 사용하고 있습니다.
 */
@Configuration
public class AWSS3Config {

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
