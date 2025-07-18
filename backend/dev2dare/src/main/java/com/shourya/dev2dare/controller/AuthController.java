package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.*;
import com.shourya.dev2dare.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register/college")
    public ResponseEntity<AuthResponse> registerCollege(@Valid @RequestBody CollegeRegisterRequest request) {
        return ResponseEntity.ok(authService.registerCollege(request));
    }

    @PostMapping("/register/student")
    public ResponseEntity<AuthResponse> registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        return ResponseEntity.ok(authService.registerStudent(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
} 