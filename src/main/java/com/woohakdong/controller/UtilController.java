package com.woohakdong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@Tag(name = "Util", description = "유틸리티 관련 API")
public class UtilController {

    @Operation(summary = "Swagger UI 리다이렉트", description = "Swagger UI 페이지로 리다이렉트합니다.")
    @GetMapping("/swagger")
    public RedirectView redirectToSwagger() {
        return new RedirectView("/swagger-ui/index.html");
    }
}
