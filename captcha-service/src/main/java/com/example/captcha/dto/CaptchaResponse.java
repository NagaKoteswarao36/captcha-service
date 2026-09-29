package com.example.captcha.dto;

public record CaptchaResponse(
        String captchaId,
        String captchaText,
        String imageBase64,
        String expiresAt
) {
}
