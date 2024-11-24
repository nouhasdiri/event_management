package com.events.event_management.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String eventName;
    private String description;
    private String location;
    private Date date;
    private String duration;
    private String category;
    private int places;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private UserApp organizer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_participants", // Nom de la table intermédiaire
            joinColumns = @JoinColumn(name = "event_id"), // Colonne pour `Event`
            inverseJoinColumns = @JoinColumn(name = "user_id") // Colonne pour `UserApp`
    )
    private List<UserApp> participants = new ArrayList<>();

}
