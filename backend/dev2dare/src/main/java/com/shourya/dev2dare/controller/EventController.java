package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.EventResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {
    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return null;
    }
} 