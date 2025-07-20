package com.shourya.dev2dare.model;

import com.shourya.dev2dare.model.Role;
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
@Table(indexes = {
    @Index(name = "idx_college_email", columnList = "email")
})
public class College {
    private static final Logger logger = LoggerFactory.getLogger(College.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String description;

    @Enumerated(EnumType.STRING)
    private Role role = Role.COLLEGE;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        logger.debug("[College] @PrePersist: College created at {}", createdAt);
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        logger.debug("[College] @PreUpdate: College updated at {}", updatedAt);
    }
}