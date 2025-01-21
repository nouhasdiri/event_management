package com.events.event_management.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user")
public class UserApp {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String username;
    private String email;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events_organize = new ArrayList<>();

    @ManyToMany(mappedBy = "participants")
    private List<Event> events_participate = new ArrayList<>();
}
