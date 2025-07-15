package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.EventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/college")
public class CollegeController {
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody EventRequest request) {
        return null;
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        return null;
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/events/{id}/registrations")
    public ResponseEntity<?> getEventRegistrations(@PathVariable Long id) {
        return null;
    }
} 