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

    @ManyToOne(optional = false)
    @JoinColumn(name = "college_id")
    private College createdBy;

    @ManyToMany(mappedBy = "registeredEvents")
    @Builder.Default
    private Set<Student> registeredStudents = new HashSet<>();
}
