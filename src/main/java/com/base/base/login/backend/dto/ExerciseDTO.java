package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para ejercicios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer pointsReward;
    private String difficulty;
    private String category;
    private Integer estimatedDurationMinutes;
    private Boolean isActive;
}
