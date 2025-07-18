package com.shourya.dev2dare.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "student_event_registration", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "event_id"})
})
public class StudentEventRegistration {
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
    protected void onRegister() { registeredAt = LocalDateTime.now(); }
} 