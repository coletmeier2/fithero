package com.fithero.fithero.model.entity;

import com.fithero.fithero.model.enums.MuscleGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.EnumMap;
import java.util.Map;

@Entity
@Table(name = "hero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer level = 1;

    @NotNull @Min(0)
    @Column(nullable = false)
    private Long experience = 0L;

    @NotNull @Min(0)
    @Column(nullable = false)
    private Integer height;

    @NotNull @Min(0)
    @Column(nullable = false)
    private Integer weight;

    @ElementCollection
    @CollectionTable(name = "hero_muscle_stats", joinColumns = @JoinColumn(name = "hero_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "muscle_group")
    private Map<MuscleGroup, MuscleAttributes> muscleStats = new EnumMap<>(MuscleGroup.class);

    @PrePersist
    void initStatsIfMissing() {
        for (MuscleGroup group : MuscleGroup.values()) {
            muscleStats.putIfAbsent(group, new MuscleAttributes(10, 10));
        }
    }
}