package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.StudentSignupRequest;
import com.shourya.dev2dare.dto.CollegeSignupRequest;
import com.shourya.dev2dare.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/student/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentSignupRequest request) {
        return null;
    }

    @PostMapping("/college/register")
    public ResponseEntity<?> registerCollege(@RequestBody CollegeSignupRequest request) {
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return null;
    }
} 