package com.woohakdong.api.controller;

import com.woohakdong.api.dto.response.PresignedUrlResponse;
import com.woohakdong.domain.util.model.ImageResourceType;
import com.woohakdong.api.facade.UtilFacade;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Tag(name = "Util", description = "유틸리티 관련 API")
public class UtilController {

    private final UtilFacade utilFacade;

    @Hidden
    @GetMapping("/swagger")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }

    @Operation(summary = "Presigned URL 획득", description = "이미지 리소스 타입에 대한 Presigned URL을 획득합니다.")
    @GetMapping("/utils/images/presigned-url")
    @ResponseBody
    public PresignedUrlResponse getPresignedUrls(
            @RequestParam ImageResourceType imageResourceType
    ) {
        return utilFacade.getPresignedUrls(imageResourceType);
    }
}
