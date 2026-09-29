package com.example.captcha.service;

import com.example.captcha.dto.CaptchaResponse;
import com.example.captcha.dto.CaptchaValidationRequest;
import com.example.captcha.entity.Captcha;
import com.example.captcha.repository.CaptchaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CaptchaService {

    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final int CODE_LENGTH = 6;
    private static final int EXPIRY_MINUTES = 5;

    private final CaptchaRepository captchaRepository;
    private final CaptchaImageService captchaImageService;
    private final SecureRandom secureRandom = new SecureRandom();

    public CaptchaService(CaptchaRepository captchaRepository,
                           CaptchaImageService captchaImageService) {
        this.captchaRepository = captchaRepository;
        this.captchaImageService = captchaImageService;
    }

    @Transactional
    public CaptchaResponse generateCaptcha() {
        String captchaId = UUID.randomUUID().toString();
        String captchaCode = generateCode();

        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = createdAt.plusMinutes(EXPIRY_MINUTES);

        Captcha captcha = new Captcha(
                captchaId,
                captchaCode,
                createdAt,
                expiresAt
        );

        captchaRepository.save(captcha);

        String imageBase64 =
                captchaImageService.generateImageBase64(captchaCode);

        return new CaptchaResponse(
                captchaId,
                captchaCode,
                imageBase64,
                expiresAt.toString()
        );
    }

    @Transactional
    public boolean validateCaptcha(CaptchaValidationRequest request) {
        Captcha captcha = captchaRepository
                .findByCaptchaId(request.captchaId())
                .orElse(null);

        if (captcha == null) {
            return false;
        }

        if (captcha.isUsed()) {
            return false;
        }

        if (LocalDateTime.now().isAfter(captcha.getExpiresAt())) {
            return false;
        }

        boolean valid = captcha.getCaptchaCode()
                .equalsIgnoreCase(request.captchaCode());

        if (valid) {
            captcha.setUsed(true);
            captchaRepository.save(captcha);
        }

        return valid;
    }

    private String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
