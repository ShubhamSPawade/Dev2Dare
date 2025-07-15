package com.shourya.dev2dare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {
    @GetMapping("/events")
    public ResponseEntity<?> getRegisteredEvents() {
        return null;
    }

    @PostMapping("/events/{id}/register")
    public ResponseEntity<?> registerForEvent(@PathVariable Long id) {
        return null;
    }
} 