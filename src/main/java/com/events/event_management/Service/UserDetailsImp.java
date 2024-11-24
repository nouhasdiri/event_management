package com.events.event_management.Service;

import com.events.event_management.Entity.UserApp;
import com.events.event_management.Repository.UserAppRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;

@Service
public class UserDetailsImp implements UserDetailsService {
    @Autowired
    private UserAppRepo userAppRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserApp> user = userAppRepo.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            // Convertir le rôle en `SimpleGrantedAuthority`
            var authority = new SimpleGrantedAuthority(userObj.getRole().getName().name());
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .authorities(authority)
                    .build();
        }
        else
            throw new UsernameNotFoundException(username);
    }
}
