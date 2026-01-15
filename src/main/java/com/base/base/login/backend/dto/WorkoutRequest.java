package com.base.base.login.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Request para crear/editar rutinas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutRequest {
    private String name;
    private String description;
    private List<WorkoutExerciseRequest> exercises;
    private Boolean favorite;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkoutExerciseRequest {
        private Long exerciseId;
        private Integer sets;
        private Integer reps;
        private Integer restSeconds;
        private Integer orderIndex;
        private String notes;
    }
}
