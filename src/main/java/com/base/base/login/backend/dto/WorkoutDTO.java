package com.base.base.login.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para rutinas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Integer totalPoints;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private Boolean favorite;
    private List<WorkoutExerciseDTO> exercises;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WorkoutExerciseDTO {
        private Long id;
        private ExerciseDTO exercise;
        private Integer sets;
        private Integer reps;
        private Integer restSeconds;
        private Integer orderIndex;
        private String notes;
        private Boolean isCompleted;
    }
}
