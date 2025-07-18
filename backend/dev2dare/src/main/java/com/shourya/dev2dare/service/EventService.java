package com.shourya.dev2dare.service;

import com.shourya.dev2dare.dto.EventRequest;
import com.shourya.dev2dare.dto.EventResponse;
import com.shourya.dev2dare.model.*;
import com.shourya.dev2dare.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final StudentRepository studentRepo;
    private final EmailService emailService;
    private final AuthService authService;

    public EventResponse create(EventRequest req, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .eventDateTime(req.getEventDateTime())
                .location(req.getLocation())
                .createdBy(college)
                .build();
        Event saved = eventRepo.save(e);

        // Notify students
        studentRepo.findAll().forEach(st -> emailService.sendEmail(
                st.getEmail(),
                "New Event: " + saved.getTitle(),
                "Hi " + st.getName() + ",\nA new event was posted by " + college.getName() + "."));

        return toDto(saved);
    }

    public EventResponse update(Long id, EventRequest req, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            throw new SecurityException("Not authorized");
        }
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setEventDateTime(req.getEventDateTime());
        e.setLocation(req.getLocation());
        return toDto(eventRepo.save(e));
    }

    public void delete(Long id, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            throw new SecurityException("Not authorized");
        }
        eventRepo.delete(e);
    }

    public List<EventResponse> listForCollege(HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        return eventRepo.findByCreatedBy(college).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private EventResponse toDto(Event e) {
        return EventResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .eventDateTime(e.getEventDateTime())
                .location(e.getLocation())
                .createdByEmail(e.getCreatedBy().getEmail())
                .build();
    }
}
