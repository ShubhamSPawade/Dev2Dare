package com.shourya.dev2dare.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
