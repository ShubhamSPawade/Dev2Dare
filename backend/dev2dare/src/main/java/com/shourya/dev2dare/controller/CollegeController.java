package com.shourya.dev2dare.controller;

import com.shourya.dev2dare.dto.EventRequest;
import com.shourya.dev2dare.dto.EventResponse;
import com.shourya.dev2dare.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/college/events")
@RequiredArgsConstructor
public class CollegeController {

    private final EventService eventService;

    @PreAuthorize("hasRole('COLLEGE')")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest req,
            HttpServletRequest httpReq) {
        return ResponseEntity.ok(eventService.create(req, httpReq));
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest req,
            HttpServletRequest httpReq) {
        return ResponseEntity.ok(eventService.update(id, req, httpReq));
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            HttpServletRequest httpReq) {
        eventService.delete(id, httpReq);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents(
            HttpServletRequest httpReq) {
        return ResponseEntity.ok(eventService.listForCollege(httpReq));
    }
}
