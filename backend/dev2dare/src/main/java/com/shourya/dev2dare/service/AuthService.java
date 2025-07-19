package com.shourya.dev2dare.service;

import com.shourya.dev2dare.dto.*;
import com.shourya.dev2dare.model.*;
import com.shourya.dev2dare.repository.*;
import com.shourya.dev2dare.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

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

    public AuthResponse registerCollege(CollegeRegisterRequest request) {
        if (collegeRepository.findByEmail(request.getEmail()).isPresent()) {
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
        return new AuthResponse(token, college.getRole().name(), college.getEmail());
    }

    public AuthResponse registerStudent(StudentRegisterRequest request) {
        if (studentRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered as a student");
        }
        College college = collegeRepository.findByName(request.getCollegeName())
                .orElseThrow(() -> new RuntimeException("College not found with name: " + request.getCollegeName()));
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .college(college)
                .role(Role.STUDENT)
                .build();
        studentRepository.save(student);
        String token = jwtUtil.generateToken(student.getEmail(), student.getRole().name());
        return new AuthResponse(token, student.getRole().name(), student.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        String role = request.getRole();
        if (role.equalsIgnoreCase("COLLEGE")) {
            College college = collegeRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("College not found"));
            if (!passwordEncoder.matches(request.getPassword(), college.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
            String token = jwtUtil.generateToken(college.getEmail(), college.getRole().name());
            return new AuthResponse(token, college.getRole().name(), college.getEmail());
        } else if (role.equalsIgnoreCase("STUDENT")) {
            Student student = studentRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
            String token = jwtUtil.generateToken(student.getEmail(), student.getRole().name());
            return new AuthResponse(token, student.getRole().name(), student.getEmail());
        } else {
            throw new RuntimeException("Invalid role");
        }
    }

    public College getLoggedInCollege(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        String email = jwtUtil.extractEmail(token); // <-- use extractEmail, not extractUsername
        return collegeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("College not found"));
    }

    public Student getLoggedInStudent(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        String email = jwtUtil.extractEmail(token);
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));
    }
}