package com.collegeevent.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping("/register-event")
    public String registerEvent() {
        return "Event registered! (Only students can access this endpoint)";
    }
} 