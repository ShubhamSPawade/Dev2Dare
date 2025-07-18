package com.shourya.dev2dare.repository;

import com.shourya.dev2dare.model.Event;
import com.shourya.dev2dare.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCreatedBy(College college);
}
