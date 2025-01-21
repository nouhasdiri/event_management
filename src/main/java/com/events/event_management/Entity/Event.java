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
    private double price;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Evaluation> evaluation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organizer_id")
    private UserApp organizer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserApp> participants = new ArrayList<>();

    public double calculateAverageRating(){
        if(evaluation == null || evaluation.isEmpty()){
            return 0.0;
        }
        int total=0;
        for(Evaluation e: evaluation){
            total+=e.getRate();
        }
        return (double) total/evaluation.size();
    }

}
