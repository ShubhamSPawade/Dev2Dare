package com.shourya.dev2dare.config;

import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    public String generateToken(String username) {
        return "dummy-jwt-token-for-" + username;
    }
} 