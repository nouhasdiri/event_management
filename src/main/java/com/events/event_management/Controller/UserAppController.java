package com.events.event_management.Controller;

import com.events.event_management.Entity.UserApp;
import com.events.event_management.Service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserAppController {
    @Autowired
    private UserAppService userAppService;

    @PostMapping("/req/signup")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String role) {
        userAppService.register(username, email, password, role);
        return "redirect:/login";
    }
}
