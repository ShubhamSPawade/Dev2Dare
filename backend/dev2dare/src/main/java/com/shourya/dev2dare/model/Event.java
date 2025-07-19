package com.shourya.dev2dare.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
    @Index(name = "idx_event_date_time", columnList = "eventDateTime"),
    @Index(name = "idx_event_college_id", columnList = "college_id"),
    @Index(name = "idx_event_status", columnList = "status")
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDateTime;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int capacity;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum EventStatus { UPCOMING, ONGOING, COMPLETED }
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.UPCOMING;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    @ManyToOne(optional = false)
    @JoinColumn(name = "college_id")
    private College createdBy;
}
