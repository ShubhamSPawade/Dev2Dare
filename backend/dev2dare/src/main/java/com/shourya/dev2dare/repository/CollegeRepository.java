package com.shourya.dev2dare.repository;

import com.shourya.dev2dare.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {
    Optional<College> findByEmail(String email);
    Optional<College> findByName(String name);
} 