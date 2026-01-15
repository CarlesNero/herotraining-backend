package com.base.base.login.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad WorkoutExercise - Relación entre Workout y Exercise
 * Almacena la configuración específica del ejercicio en la rutina
 */
@Entity
@Table(name = "workout_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    @Builder.Default
    private Integer sets = 3;

    @Column(nullable = false)
    @Builder.Default
    private Integer reps = 10;

    @Column(name = "rest_seconds")
    @Builder.Default
    private Integer restSeconds = 60;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;
}
