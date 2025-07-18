package com.shourya.dev2dare.repository;

import com.shourya.dev2dare.model.StudentEventRegistration;
import com.shourya.dev2dare.model.Student;
import com.shourya.dev2dare.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentEventRegistrationRepository extends JpaRepository<StudentEventRegistration, Long> {
    List<StudentEventRegistration> findByStudent(Student student);
    List<StudentEventRegistration> findByEvent(Event event);
    Optional<StudentEventRegistration> findByStudentAndEvent(Student student, Event event);
    void deleteByStudentAndEvent(Student student, Event event);
    Optional<StudentEventRegistration> findFirstByEventAndWaitlistedOrderByRegisteredAtAsc(Event event, boolean waitlisted);
} 