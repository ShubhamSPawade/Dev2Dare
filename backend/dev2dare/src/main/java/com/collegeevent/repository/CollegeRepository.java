package com.collegeevent.repository;

import com.collegeevent.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CollegeRepository extends JpaRepository<College, Long> {
    Optional<College> findByEmail(String email);
} 