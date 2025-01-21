package com.events.event_management.Repository;

import com.events.event_management.Entity.Evaluation;
import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepo extends JpaRepository<Evaluation, Integer> {
    Optional<Evaluation> findByUserAndEvent(UserApp user, Event event);
}
