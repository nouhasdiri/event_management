package com.events.event_management.Controller;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Service.EventService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/add-event")
    public String addEvent(@RequestParam("eventName") String eventName,
                           @RequestParam("description") String description,
                           @RequestParam("location") String location,
                           @RequestParam("dateTime") String dateTime,
                           @RequestParam("duration") String duration,
                           @RequestParam("category") String category,
                           @RequestParam("places") int places
    ) {
        System.out.println(eventName);
        Date eventDateTime;
        try {
            eventDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(dateTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Format de date et heure incorrect : " + dateTime);
        }
        eventService.addEvent(eventName, description, location, eventDateTime, duration, category, places);
        return "redirect:/organizer_dashboard";
    }

    @GetMapping("/participant_dashboard")
    public String events(Model model) {
        List<Event> events = eventService.getAllEvents();
        events.forEach(event -> {
            System.out.println("Event: " + event.getEventName());
            System.out.println(event.getParticipants().size());
        });
        model.addAttribute("events", events);
        return "participant_dashboard";
    }

    @PostMapping("/participate")
    public String participate(@RequestParam("id_event") long id_event) {
        Event event = eventService.findEventById(id_event)
                .orElseThrow(() -> new NoSuchElementException("event not found : "));
        eventService.participate(event);
        return "redirect:/participant_dashboard";
    }

    @GetMapping("/organizer_dashboard")
    public String organisateur(Model model){
        System.out.println("nouhaila help");
        List<Event> events = eventService.findEventByOrganizer();
        // Log pour debug
        events.forEach(event -> {
            System.out.println("Event: " + event.getEventName());
            System.out.println(event.getParticipants().size());
        });
        model.addAttribute("events", events);
        return "organizer_dashboard";
    }

}