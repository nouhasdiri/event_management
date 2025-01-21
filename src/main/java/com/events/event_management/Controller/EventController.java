package com.events.event_management.Controller;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.EventWithRate;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Repository.UserAppRepo;
import com.events.event_management.Service.EventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserAppRepo userAppRepo;

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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        List<Event> events = eventService.getAllEvents();
        UserApp user = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("user not found : "));
        System.out.println(userDetails.getUsername());
        List<EventWithRate> eventsWithRates = new ArrayList<>();
        for (Event event : events) {
            int rate = eventService.findRate(event, user);
            eventsWithRates.add(new EventWithRate(event, rate));
        }
        model.addAttribute("eventsWithRates", eventsWithRates);
        model.addAttribute("username", user.getUsername());
        return "participant_dashboard";
    }

    @PostMapping("/participate")
    public String participate(@RequestParam("id_event") long id_event, RedirectAttributes redirectAttributes) {
        try {
            Event event = eventService.findEventById(id_event)
                    .orElseThrow(() -> new NoSuchElementException("event not found : "));
            eventService.participate(event);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/participant_dashboard";  // Ou toute autre page où vous voulez afficher l'erreur
        }
        return "redirect:/participant_dashboard";
    }



    @GetMapping("/organizer_dashboard")
    public String organisateur(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        List<Event> events = eventService.findEventByOrganizer();
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("events", events);
        return "organizer_dashboard";
    }

    @PostMapping("/delete-event")
    public String deleteEvent(@RequestParam("id_event") long event_id){
        Event event = eventService.findEventById(event_id)
                .orElseThrow(() -> new NoSuchElementException("event not found : "));
        eventService.deleteEvent(event);
        return "redirect:/organizer_dashboard";
    }

    @PostMapping("/rate-event")
    public String rateEvent(@RequestParam("id") long id, @RequestParam("rate") int rate){
        System.out.println(rate);
        Event event = eventService.findEventById(id)
                .orElseThrow(() -> new NoSuchElementException("event not found : "));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        UserApp user = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("user not found : "));
        eventService.rateEvent(event, user, rate);
        return "redirect:/participant_dashboard";
    }



}