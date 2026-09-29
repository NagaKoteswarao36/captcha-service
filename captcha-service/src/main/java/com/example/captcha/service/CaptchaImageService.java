package com.example.captcha.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class CaptchaImageService {

    public String generateImageBase64(String captchaText) {
        int width = 220;
        int height = 80;

        BufferedImage image = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics = image.createGraphics();

        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);

            graphics.setFont(new Font("Arial", Font.BOLD, 38));
            graphics.setColor(Color.BLACK);
            graphics.drawString(captchaText, 35, 53);

            // Simple visual noise
            graphics.setStroke(new BasicStroke(2));
            for (int i = 0; i < 8; i++) {
                int x1 = (i * 31) % width;
                int y1 = 10 + (i * 7) % height;
                int x2 = (x1 + 60) % width;
                int y2 = 10 + (i * 19) % height;
                graphics.drawLine(x1, y1, x2, y2);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (IOException e) {
            throw new IllegalStateException("Unable to generate CAPTCHA image", e);
        } finally {
            graphics.dispose();
        }
    }
}
