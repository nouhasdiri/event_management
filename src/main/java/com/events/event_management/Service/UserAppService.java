package com.events.event_management.Service;

import com.events.event_management.Entity.Event;
import com.events.event_management.Entity.Role;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Enums.RoleName;
import com.events.event_management.Repository.RoleRepo;
import com.events.event_management.Repository.UserAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAppService {
    @Autowired
    private UserAppRepo userAppRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void register(String username, String email, String password, String roleName) {
        UserApp userApp = new UserApp();
        userApp.setEmail(email);
        userApp.setPassword(passwordEncoder.encode(password));
        userApp.setUsername(username);

        Role role = roleRepo.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found"));
        userApp.setRole(role);
        userAppRepo.save(userApp);
        emailService.sendEmail(email, "ConnectNow Team", "Hello "+username+ ",\n\n" +
                "Thank you for creating an account with us. We're thrilled to have you onboard!\n\n" +
                "Enjoy our platform.\n\n" +
                "Best regards,\nConnectNow Team");
    }

    public Optional<UserApp> getUserById(long id) {
        return userAppRepo.findById(id);
    }

    public List<Event> getEvents(UserApp user) {
        return userAppRepo.findEventsByUser(user);
    }
}
