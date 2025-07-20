package com.shourya.dev2dare.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_event_registration", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "event_id"})
})
public class StudentEventRegistration {
    private static final Logger logger = LoggerFactory.getLogger(StudentEventRegistration.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    private boolean waitlisted = false;

    @PrePersist
    protected void onRegister() {
        registeredAt = LocalDateTime.now();
        logger.debug("[StudentEventRegistration] @PrePersist: Registration at {}", registeredAt);
    }
} 