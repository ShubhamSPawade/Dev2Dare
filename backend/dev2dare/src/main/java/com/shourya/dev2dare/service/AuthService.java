package com.shourya.dev2dare.service;

import com.shourya.dev2dare.dto.*;
import com.shourya.dev2dare.model.*;
import com.shourya.dev2dare.repository.*;
import com.shourya.dev2dare.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse registerCollege(CollegeRegisterRequest request) {
        logger.info("[AuthService] Registering college: email='{}'", request.getEmail());
        if (collegeRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("[AuthService] Email already registered as a college: {}", request.getEmail());
            throw new RuntimeException("Email already registered as a college");
        }
        College college = College.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .description(request.getDescription())
                .role(Role.COLLEGE)
                .build();
        collegeRepository.save(college);
        String token = jwtUtil.generateToken(college.getEmail(), college.getRole().name());
        logger.info("[AuthService] College registered successfully: email='{}'", request.getEmail());
        return new AuthResponse(token, college.getRole().name(), college.getEmail());
    }

    public AuthResponse registerStudent(StudentRegisterRequest request) {
        logger.info("[AuthService] Registering student: email='{}'", request.getEmail());
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("[AuthService] Email already registered as a student: {}", request.getEmail());
            throw new RuntimeException("Email already registered as a student");
        }
        College college = collegeRepository.findByName(request.getCollegeName())
                .orElseThrow(() -> {
                    logger.error("[AuthService] College not found with name: {}", request.getCollegeName());
                    return new RuntimeException("College not found with name: " + request.getCollegeName());
                });
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .college(college)
                .role(Role.STUDENT)
                .build();
        studentRepository.save(student);
        String token = jwtUtil.generateToken(student.getEmail(), student.getRole().name());
        logger.info("[AuthService] Student registered successfully: email='{}'", request.getEmail());
        return new AuthResponse(token, student.getRole().name(), student.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        logger.info("[AuthService] Login attempt: email='{}', role='{}'", request.getEmail(), request.getRole());
        String role = request.getRole();
        if (role.equalsIgnoreCase("COLLEGE")) {
            College college = collegeRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        logger.error("[AuthService] College not found: {}", request.getEmail());
                        return new RuntimeException("College not found");
                    });
            if (!passwordEncoder.matches(request.getPassword(), college.getPassword())) {
                logger.warn("[AuthService] Invalid credentials for college: {}", request.getEmail());
                throw new RuntimeException("Invalid credentials");
            }
            String token = jwtUtil.generateToken(college.getEmail(), college.getRole().name());
            logger.info("[AuthService] College login successful: {}", request.getEmail());
            return new AuthResponse(token, college.getRole().name(), college.getEmail());
        } else if (role.equalsIgnoreCase("STUDENT")) {
            Student student = studentRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        logger.error("[AuthService] Student not found: {}", request.getEmail());
                        return new RuntimeException("Student not found");
                    });
            if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
                logger.warn("[AuthService] Invalid credentials for student: {}", request.getEmail());
                throw new RuntimeException("Invalid credentials");
            }
            String token = jwtUtil.generateToken(student.getEmail(), student.getRole().name());
            logger.info("[AuthService] Student login successful: {}", request.getEmail());
            return new AuthResponse(token, student.getRole().name(), student.getEmail());
        } else {
            logger.error("[AuthService] Invalid role: {}", role);
            throw new RuntimeException("Invalid role");
        }
    }

    public College getLoggedInCollege(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        String email = jwtUtil.extractEmail(token); // <-- use extractEmail, not extractUsername
        logger.debug("[AuthService] Getting logged-in college for email={}", email);
        return collegeRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("[AuthService] College not found for email={}", email);
                    return new UsernameNotFoundException("College not found");
                });
    }

    public Student getLoggedInStudent(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        String email = jwtUtil.extractEmail(token);
        logger.debug("[AuthService] Getting logged-in student for email={}", email);
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("[AuthService] Student not found for email={}", email);
                    return new UsernameNotFoundException("Student not found");
                });
    }
}