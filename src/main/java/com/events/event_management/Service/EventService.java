package com.events.event_management.Service;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Repository.EventRepo;
import com.events.event_management.Repository.UserAppRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private UserAppRepo userAppRepo;

    public Optional<Event> findEventById(Long id) {
        return eventRepo.findById(id);
    }

    public List<Event> findEventByOrganizer(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        UserApp organizer = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé : " + userDetails.getUsername()));
        return eventRepo.findByOrganizer(organizer);
    }

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }
    public Event addEvent(String eventName,
                          String description,
                          String location,
                          Date date,
                          String duration,
                          String category,
                          int places
                          ) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        UserApp organizer = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Utilisateur non trouvé : " + userDetails.getUsername()));
        Event event = new Event();
        event.setEventName(eventName);
        event.setDescription(description);
        event.setLocation(location);
        event.setDate(date);
        event.setDuration(duration);
        event.setCategory(category);
        event.setPlaces(places);
        event.setOrganizer(organizer);
        return eventRepo.save(event);
    }

    public void participate(Event event){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        UserApp participant = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("user not found : " + userDetails.getUsername()));
        if (event.getParticipants().contains(participant)) {
            throw new IllegalArgumentException("Vous avez déjà participé à cet événement.");
        }
        event.getParticipants().add(participant);
        eventRepo.save(event);
    }


}
