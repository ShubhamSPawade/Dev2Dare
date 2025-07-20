package com.shourya.dev2dare.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        logger.info("[EmailServiceImpl] Sending email to: {} | Subject: {}", to, subject);
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            logger.info("[EmailServiceImpl] Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("[EmailServiceImpl] Failed to send email to: {} | Error: {}", to, e.getMessage());
            throw e;
        }
    }
}
