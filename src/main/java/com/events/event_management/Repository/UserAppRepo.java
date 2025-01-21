package com.events.event_management.Repository;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAppRepo extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByUsername(String userName);
    @Query("SELECT u.events_participate FROM UserApp u WHERE u = :user")
    List<Event> findEventsByUser(UserApp user);
}
