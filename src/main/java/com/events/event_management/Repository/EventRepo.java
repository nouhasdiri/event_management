package com.events.event_management.Repository;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(UserApp organizer);
}
