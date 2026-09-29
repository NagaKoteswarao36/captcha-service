package com.example.captcha.controller;

import com.example.captcha.dto.CaptchaResponse;
import com.example.captcha.dto.CaptchaValidationRequest;
import com.example.captcha.service.CaptchaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<CaptchaResponse> generateCaptcha() {
        return ResponseEntity.ok(captchaService.generateCaptcha());
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateCaptcha(
            @Valid @RequestBody CaptchaValidationRequest request) {

        boolean valid = captchaService.validateCaptcha(request);

        return ResponseEntity.ok(Map.of(
                "captchaId", request.captchaId(),
                "valid", valid,
                "message", valid
                        ? "CAPTCHA validation successful"
                        : "Invalid or expired CAPTCHA"
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "service", "captcha-service",
                "status", "UP"
        ));
    }
}
