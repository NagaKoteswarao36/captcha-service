package com.example.captcha.repository;

import com.example.captcha.entity.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaptchaRepository extends JpaRepository<Captcha, Long> {

    Optional<Captcha> findByCaptchaId(String captchaId);
}
