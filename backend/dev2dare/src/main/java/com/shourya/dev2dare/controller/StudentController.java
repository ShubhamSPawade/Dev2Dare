package com.shourya.dev2dare.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import com.shourya.dev2dare.service.EventService;
import com.shourya.dev2dare.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.shourya.dev2dare.dto.EventResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private EventService eventService;
    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/register-event")
    public String registerEvent(@RequestParam Long eventId, HttpServletRequest request) {
        logger.info("[StudentController] Registering for event: eventId={}", eventId);
        var student = authService.getLoggedInStudent(request);
        String result = eventService.registerStudentToEvent(student.getId(), eventId);
        logger.info("[StudentController] Registration result for studentId={}, eventId={}: {}", student.getId(), eventId, result);
        return result;
    }

    @GetMapping("/events")
    public List<EventResponse> getAllEvents() {
        logger.info("[StudentController] Fetching all events");
        return eventService.getAllEvents();
    }

    @GetMapping("/my-events")
    public List<EventResponse> getMyEvents(HttpServletRequest request) {
        var student = authService.getLoggedInStudent(request);
        logger.info("[StudentController] Fetching events for studentId={}", student.getId());
        return eventService.getEventsForStudent(student.getId());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/cancel-registration")
    public String cancelRegistration(@RequestParam Long eventId, HttpServletRequest request) {
        var student = authService.getLoggedInStudent(request);
        logger.info("[StudentController] Cancelling registration for studentId={}, eventId={}", student.getId(), eventId);
        String result = eventService.cancelRegistration(student.getId(), eventId);
        logger.info("[StudentController] Cancel registration result for studentId={}, eventId={}: {}", student.getId(), eventId, result);
        return result;
    }
} 