package com.shourya.dev2dare.service;

import com.shourya.dev2dare.dto.EventRequest;
import com.shourya.dev2dare.dto.EventResponse;
import com.shourya.dev2dare.model.College;
import com.shourya.dev2dare.model.Event;
import com.shourya.dev2dare.model.Student;
import com.shourya.dev2dare.repository.EventRepository;
import com.shourya.dev2dare.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;
    private final EmailService emailService;
    private final AuthService authService;

    /**
     * Creates a new event and notifies all students via email.
     */
    public EventResponse create(EventRequest req, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .eventDateTime(req.getEventDateTime())
                .location(req.getLocation())
                .createdBy(college)
                .build();
        Event saved = eventRepository.save(e);

        // Notify students
        studentRepository.findAll().forEach(student -> emailService.sendEmail(
                student.getEmail(),
                "New Event: " + saved.getTitle(),
                "Hi " + student.getName() + ",\nA new event was posted by " + college.getName() + "."));

        return toDto(saved);
    }

    /**
     * Updates an existing event if the logged-in college is the creator.
     */
    public EventResponse update(Long id, EventRequest req, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            throw new SecurityException("Not authorized");
        }
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setEventDateTime(req.getEventDateTime());
        e.setLocation(req.getLocation());
        Event updated = eventRepository.save(e);
        return toDto(updated);
    }

    /**
     * Deletes an event if the logged-in college is the creator.
     */
    public void delete(Long id, HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            throw new SecurityException("Not authorized");
        }
        eventRepository.delete(e);
    }

    /**
     * Lists events created by the logged-in college.
     */
    public List<EventResponse> listForCollege(HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        return eventRepository.findByCreatedBy(college).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a student for an event. Throws if student or event not found.
     */
    public String registerStudentToEvent(Long studentId, Long eventId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);

        if (studentOpt.isEmpty()) {
            throw new EntityNotFoundException("Student not found");
        }
        if (eventOpt.isEmpty()) {
            throw new EntityNotFoundException("Event not found");
        }

        Student student = studentOpt.get();
        Event event = eventOpt.get();

        if (student.getRegisteredEvents().contains(event)) {
            return "Student already registered for this event.";
        }

        student.getRegisteredEvents().add(event);
        studentRepository.save(student);
        return "Registration successful!";
    }

    /**
     * Converts Event entity to DTO.
     */
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
