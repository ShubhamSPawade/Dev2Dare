package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.EventRegistrationRequest;
import com.shourya.dev2dare.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/register")
    public ResponseEntity<String> registerStudentToEvent(@RequestBody EventRegistrationRequest request) {
        String message = eventService.registerStudentToEvent(request.getStudentId(), request.getEventId());
        return ResponseEntity.ok(message);
    }
}
