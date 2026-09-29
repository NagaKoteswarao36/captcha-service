package com.example.captcha.dto;

import jakarta.validation.constraints.NotBlank;

public record CaptchaValidationRequest(
        @NotBlank(message = "captchaId is required")
        String captchaId,

        @NotBlank(message = "captchaCode is required")
        String captchaCode
) {
}
