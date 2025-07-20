package com.shourya.dev2dare.service;

import com.shourya.dev2dare.dto.EventRequest;
import com.shourya.dev2dare.dto.EventResponse;
import com.shourya.dev2dare.model.College;
import com.shourya.dev2dare.model.Event;
import com.shourya.dev2dare.model.Student;
import com.shourya.dev2dare.model.StudentEventRegistration;
import com.shourya.dev2dare.repository.EventRepository;
import com.shourya.dev2dare.repository.StudentEventRegistrationRepository;
import com.shourya.dev2dare.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;
    private final StudentEventRegistrationRepository registrationRepository;
    private final EmailService emailService;
    private final AuthService authService;

    /**
     * Creates a new event and notifies all students via email.
     */
    public EventResponse create(EventRequest req, HttpServletRequest httpReq) {
        logger.info("[EventService] Creating event: title='{}', by college from request.", req.getTitle());
        College college = authService.getLoggedInCollege(httpReq);
        Event e = Event.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .eventDateTime(req.getEventDateTime())
                .location(req.getLocation())
                .capacity(req.getCapacity())
                .createdBy(college)
                .build();
        Event saved = eventRepository.save(e);
        logger.info("[EventService] Event saved: id={}, title='{}'", saved.getId(), saved.getTitle());

        // Notify students
        studentRepository.findAll().forEach(student -> {
            emailService.sendEmail(
                student.getEmail(),
                "New Event: " + saved.getTitle(),
                "Hi " + student.getName() + ",\nA new event was posted by " + college.getName() + ".");
            logger.debug("[EventService] Notified student: {}", student.getEmail());
        });

        logger.info("[EventService] Event creation complete for id={}", saved.getId());
        return toDto(saved);
    }

    /**
     * Updates an existing event if the logged-in college is the creator.
     */
    public EventResponse update(Long id, EventRequest req, HttpServletRequest httpReq) {
        logger.info("[EventService] Updating event: id={}", id);
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[EventService] Event not found: id={}", id);
                    return new EntityNotFoundException("Event not found");
                });
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            logger.error("[EventService] Unauthorized update attempt by college id={}", college.getId());
            throw new SecurityException("Not authorized");
        }
        e.setTitle(req.getTitle());
        e.setDescription(req.getDescription());
        e.setEventDateTime(req.getEventDateTime());
        e.setLocation(req.getLocation());
        e.setCapacity(req.getCapacity());
        Event updated = eventRepository.save(e);
        logger.info("[EventService] Event updated: id={}", updated.getId());
        return toDto(updated);
    }

    /**
     * Deletes an event if the logged-in college is the creator.
     */
    public void delete(Long id, HttpServletRequest httpReq) {
        logger.info("[EventService] Deleting event: id={}", id);
        College college = authService.getLoggedInCollege(httpReq);
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("[EventService] Event not found for delete: id={}", id);
                    return new EntityNotFoundException("Event not found");
                });
        if (!e.getCreatedBy().getId().equals(college.getId())) {
            logger.error("[EventService] Unauthorized delete attempt by college id={}", college.getId());
            throw new SecurityException("Not authorized");
        }
        eventRepository.delete(e);
        logger.info("[EventService] Event deleted: id={}", id);
    }

    /**
     * Lists events created by the logged-in college.
     */
    public List<EventResponse> listForCollege(HttpServletRequest httpReq) {
        College college = authService.getLoggedInCollege(httpReq);
        logger.info("[EventService] Listing events for college id={}", college.getId());
        return eventRepository.findByCreatedBy(college).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Registers a student for an event. Throws if student or event not found.
     */
    public String registerStudentToEvent(Long studentId, Long eventId) {
        logger.info("[EventService] Registering student id={} to event id={}", studentId, eventId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("[EventService] Student not found: id={}", studentId);
                    return new EntityNotFoundException("Student not found");
                });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    logger.error("[EventService] Event not found for registration: id={}", eventId);
                    return new EntityNotFoundException("Event not found");
                });
        if (registrationRepository.findByStudentAndEvent(student, event).isPresent()) {
            logger.warn("[EventService] Student id={} already registered for event id={}", studentId, eventId);
            return "Student already registered for this event.";
        }
        long registeredCount = registrationRepository.findByEvent(event).stream().filter(r -> !r.isWaitlisted()).count();
        boolean waitlisted = registeredCount >= event.getCapacity();
        StudentEventRegistration registration = StudentEventRegistration.builder()
                .student(student)
                .event(event)
                .waitlisted(waitlisted)
                .build();
        registrationRepository.save(registration);
        if (waitlisted) {
            logger.info("[EventService] Student id={} waitlisted for event id={}", studentId, eventId);
            return "Event is full. You have been added to the waitlist.";
        } else {
            logger.info("[EventService] Student id={} successfully registered for event id={}", studentId, eventId);
            return "Registration successful!";
        }
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return registrationRepository.findByStudent(student).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<String> getRegisteredStudentsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return registrationRepository.findByEvent(event).stream()
                .map(reg -> reg.getStudent().getName() + " (" + reg.getStudent().getEmail() + ")")
                .collect(Collectors.toList());
    }

    public List<String> getWaitlistedStudentsForEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        return registrationRepository.findByEvent(event).stream()
                .filter(StudentEventRegistration::isWaitlisted)
                .map(reg -> reg.getStudent().getName() + " (" + reg.getStudent().getEmail() + ")")
                .collect(Collectors.toList());
    }

    @Transactional
    public String cancelRegistration(Long studentId, Long eventId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Optional<StudentEventRegistration> regOpt = registrationRepository.findByStudentAndEvent(student, event);
        if (regOpt.isEmpty()) {
            return "You are not registered for this event.";
        }
        boolean wasWaitlisted = regOpt.get().isWaitlisted();
        registrationRepository.deleteByStudentAndEvent(student, event);
        if (!wasWaitlisted) {
            // Promote the earliest waitlisted student
            Optional<StudentEventRegistration> nextInLine = registrationRepository
                .findFirstByEventAndWaitlistedOrderByRegisteredAtAsc(event, true);
            if (nextInLine.isPresent()) {
                StudentEventRegistration promoted = nextInLine.get();
                promoted.setWaitlisted(false);
                registrationRepository.save(promoted);
                // Optionally, notify the student (e.g., by email)
                return "Registration cancelled. " + promoted.getStudent().getName() + " has been promoted from the waitlist.";
            }
        }
        return "Registration cancelled.";
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
                .status(e.getStatus() != null ? e.getStatus().name() : null)
                .registrationTimestamp(null)
                .build();
    }

    private EventResponse toDto(StudentEventRegistration reg) {
        Event e = reg.getEvent();
        return EventResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .eventDateTime(e.getEventDateTime())
                .location(e.getLocation())
                .createdByEmail(e.getCreatedBy().getEmail())
                .status(e.getStatus() != null ? e.getStatus().name() : null)
                .registrationTimestamp(reg.getRegisteredAt() != null ? reg.getRegisteredAt().toString() : null)
                .waitlisted(reg.isWaitlisted())
                .build();
    }
}
