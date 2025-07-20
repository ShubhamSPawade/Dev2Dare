package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.*;
import com.shourya.dev2dare.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register/college")
    public ResponseEntity<AuthResponse> registerCollege(@Valid @RequestBody CollegeRegisterRequest request) {
        logger.info("[AuthController] Registering college: email='{}'", request.getEmail());
        ResponseEntity<AuthResponse> response = ResponseEntity.ok(authService.registerCollege(request));
        logger.info("[AuthController] College registered successfully: email='{}'", request.getEmail());
        return response;
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthResponse> registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        logger.info("[AuthController] Registering student: email='{}'", request.getEmail());
        ResponseEntity<AuthResponse> response = ResponseEntity.ok(authService.registerStudent(request));
        logger.info("[AuthController] Student registered successfully: email='{}'", request.getEmail());
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("[AuthController] Login attempt: email='{}', role='{}'", request.getEmail(), request.getRole());
        ResponseEntity<AuthResponse> response = ResponseEntity.ok(authService.login(request));
        logger.info("[AuthController] Login successful: email='{}', role='{}'", request.getEmail(), request.getRole());
        return response;
    }
} 