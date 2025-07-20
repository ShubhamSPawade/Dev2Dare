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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/college/events")
@RequiredArgsConstructor
public class CollegeController {

    private static final Logger logger = LoggerFactory.getLogger(CollegeController.class);
    private final EventService eventService;

    @PreAuthorize("hasRole('COLLEGE')")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest req,
            HttpServletRequest httpReq) {
        logger.info("[CollegeController] Received request to create event: title='{}'", req.getTitle());
        ResponseEntity<EventResponse> response = ResponseEntity.ok(eventService.create(req, httpReq));
        logger.info("[CollegeController] Event created successfully: title='{}'", req.getTitle());
        return response;
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequest req,
            HttpServletRequest httpReq) {
        logger.info("[CollegeController] Received request to update event: id={}", id);
        ResponseEntity<EventResponse> response = ResponseEntity.ok(eventService.update(id, req, httpReq));
        logger.info("[CollegeController] Event updated successfully: id={}", id);
        return response;
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long id,
            HttpServletRequest httpReq) {
        logger.info("[CollegeController] Received request to delete event: id={}", id);
        eventService.delete(id, httpReq);
        logger.info("[CollegeController] Event deleted successfully: id={}", id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents(
            HttpServletRequest httpReq) {
        logger.info("[CollegeController] Listing events for logged-in college");
        return ResponseEntity.ok(eventService.listForCollege(httpReq));
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @GetMapping("/{id}/registrations")
    public ResponseEntity<List<String>> getRegisteredStudents(@PathVariable Long id) {
        logger.info("[CollegeController] Getting registered students for event id={}", id);
        return ResponseEntity.ok(eventService.getRegisteredStudentsForEvent(id));
    }

    @PreAuthorize("hasRole('COLLEGE')")
    @GetMapping("/{id}/waitlist")
    public ResponseEntity<List<String>> getWaitlistedStudents(@PathVariable Long id) {
        logger.info("[CollegeController] Getting waitlisted students for event id={}", id);
        return ResponseEntity.ok(eventService.getWaitlistedStudentsForEvent(id));
    }
}
