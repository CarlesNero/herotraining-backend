package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Request para crear/editar ejercicios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequest {
    private String name;
    private String description;
    private Integer pointsReward;
    private String difficulty; // VERY_EASY, EASY, MEDIUM, HARD, VERY_HARD
    private String category; // CARDIO, STRENGTH, FLEXIBILITY, etc.
    private Integer estimatedDurationMinutes;
}
