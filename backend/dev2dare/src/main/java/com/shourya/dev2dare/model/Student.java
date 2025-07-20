package com.shourya.dev2dare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
    @Index(name = "idx_student_email", columnList = "email")
})
public class Student {

    private static final Logger logger = LoggerFactory.getLogger(Student.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @ManyToOne
    @JoinColumn(name = "college_id")
    private College college;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.STUDENT;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        logger.debug("[Student] @PrePersist: Student created at {}", createdAt);
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        logger.debug("[Student] @PreUpdate: Student updated at {}", updatedAt);
    }
}