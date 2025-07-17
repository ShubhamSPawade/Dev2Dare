package com.shourya.dev2dare.model;

import com.shourya.dev2dare.model.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String collegeName;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;
} 