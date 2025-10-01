package com.fithero.fithero.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    /*
     * Consider adding a fatigue field to the workout, because some of the compound movements will hit more muscle,
     * penalty of more fatigue, e.g. deadlift
     */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Hero hero;
    
    @ManyToOne
    private Exercise exercise;
    
    private Integer sets;
    private Integer reps;
    private Double weight;
    private LocalDateTime performedAt;
    private String notes;
}