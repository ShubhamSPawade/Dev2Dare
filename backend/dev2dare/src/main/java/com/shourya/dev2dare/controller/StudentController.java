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

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private EventService eventService;
    @Autowired
    private AuthService authService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/register-event")
    public String registerEvent(@RequestParam Long eventId, HttpServletRequest request) {
        var student = authService.getLoggedInStudent(request);
        return eventService.registerStudentToEvent(student.getId(), eventId);
    }

    @GetMapping("/events")
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/my-events")
    public List<EventResponse> getMyEvents(HttpServletRequest request) {
        var student = authService.getLoggedInStudent(request);
        return eventService.getEventsForStudent(student.getId());
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/cancel-registration")
    public String cancelRegistration(@RequestParam Long eventId, HttpServletRequest request) {
        var student = authService.getLoggedInStudent(request);
        return eventService.cancelRegistration(student.getId(), eventId);
    }
} 