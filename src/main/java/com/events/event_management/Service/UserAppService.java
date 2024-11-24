package com.events.event_management.Service;

import com.events.event_management.Entity.Role;
import com.events.event_management.Entity.UserApp;
import com.events.event_management.Enums.RoleName;
import com.events.event_management.Repository.RoleRepo;
import com.events.event_management.Repository.UserAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAppService {
    @Autowired
    private UserAppRepo userAppRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserApp register(String username, String email, String password, String roleName) {
        UserApp userApp = new UserApp();
        userApp.setEmail(email);
        userApp.setPassword(passwordEncoder.encode(password));
        userApp.setUsername(username);

        Role role = roleRepo.findByName(RoleName.valueOf(roleName))
                .orElseThrow(() -> new RuntimeException("Role not found"));
        userApp.setRole(role);
        return userAppRepo.save(userApp);

    }
}
