package com.events.event_management.Repository;

import com.events.event_management.Entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAppRepo extends JpaRepository<UserApp, Long> {
    Optional<UserApp> findByUsername(String userName);
}
