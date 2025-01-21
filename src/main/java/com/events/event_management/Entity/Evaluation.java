package com.events.event_management.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Event event;
    @ManyToOne
    private UserApp user;
    private int rate=0;
}
