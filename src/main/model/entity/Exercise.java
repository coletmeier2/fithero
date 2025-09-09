package com.fithero.fithero.model.entity;

import com.fithero.fithero.model.enums.ExerciseType;
import com.fithero.fithero.model.enums.MuscleGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.EnumMap;
import java.util.Map;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType exerciseType;

    // Optional: a single primary group; for multi-target use targetWeights below
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @ElementCollection
    @CollectionTable(name = "exercise_targets", joinColumns = @JoinColumn(name = "exercise_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "muscle_group")
    @Column(name = "weight_percent")
    private Map<MuscleGroup, Integer> targetWeights = new EnumMap<>(MuscleGroup.class);
}