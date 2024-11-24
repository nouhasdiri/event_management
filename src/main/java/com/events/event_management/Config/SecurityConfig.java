package com.events.event_management.Config;

import com.events.event_management.Entity.Role;
import com.events.event_management.Enums.RoleName;
import com.events.event_management.Handler.CustomAuthenticationSuccessHandler;
import com.events.event_management.Repository.RoleRepo;
import com.events.event_management.Service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableAutoConfiguration
public class SecurityConfig {
    @Autowired
    private UserDetailsImp userDetailsImp;


    @Bean
    public UserDetailsService userDetailsService(){
        return userDetailsImp;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsImp);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner dataLoader(RoleRepo roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role organiserRole = new Role();
                organiserRole.setName(RoleName.ROLE_ORGANISATEUR);
                roleRepository.save(organiserRole);

                Role participantRole = new Role();
                participantRole.setName(RoleName.ROLE_PARTICIPANT);
                roleRepository.save(participantRole);
            }
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/req/login").permitAll();
                    // Utilisation de votre CustomAuthenticationSuccessHandler
                    httpForm.successHandler(new CustomAuthenticationSuccessHandler());
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout") // URL pour se déconnecter
                            .logoutSuccessUrl("/req/login?logout") // URL après la déconnexion réussie
                            .invalidateHttpSession(true) // Invalide la session HTTP
                            .deleteCookies("JSESSIONID") // Supprime le cookie de session
                            .permitAll();
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/","/index","/req/signup", "/css/**", "/js/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .build();
    }
}