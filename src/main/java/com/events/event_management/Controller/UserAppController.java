package com.events.event_management.Controller;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Repository.UserAppRepo;
import com.events.event_management.Service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class UserAppController {
    @Autowired
    private UserAppService userAppService;
    @Autowired
    private UserAppRepo userAppRepo;

    @PostMapping("/req/signup")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role) {
        userAppService.register(username, email, password, role);
        return "redirect:/req/login";
    }

    @GetMapping("/events")
    public String events(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        UserApp user = userAppRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("user not found : "));
        List<Event> events = userAppService.getEvents(user);
        System.out.println("events size "+ events.size());
        model.addAttribute("events", events);
        return "events";
    }

}
