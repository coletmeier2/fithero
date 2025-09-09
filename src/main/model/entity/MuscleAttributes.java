package com.fithero.fithero.model.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MuscleAttributes {
    @Column(nullable = false)
    private int strength;
    @Column(nullable = false)
    private int stamina;
}
