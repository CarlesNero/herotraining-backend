package com.base.base.login.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Exercise - Ejercicios que se pueden añadir a rutinas
 */
@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "points_reward", nullable = false)
    @Builder.Default
    private Integer pointsReward = 10;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ExerciseDifficulty difficulty = ExerciseDifficulty.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ExerciseCategory category = ExerciseCategory.CARDIO;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ExerciseDifficulty {
        VERY_EASY,
        EASY,
        MEDIUM,
        HARD,
        VERY_HARD
    }

    public enum ExerciseCategory {
        CARDIO,
        STRENGTH,
        FLEXIBILITY,
        BALANCE,
        ENDURANCE,
        HIIT,
        YOGA,
        CROSSFIT
    }
}
