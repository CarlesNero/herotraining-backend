package com.base.base.login.backend.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad User - Sincronizada con Keycloak
 * Almacena información adicional del usuario no gestionada por Keycloak
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private static final int BASE_POINTS = 100;
    private static final int MAX_LEVEL = 99;

    @Id
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId; // UUID de Keycloak

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    // Campos específicos de HeroTraining
    @Column(name = "total_points")
    @Builder.Default
    private Integer totalPoints = 0;

    @Column(name = "current_level")
    @Builder.Default
    private Integer currentLevel = 1;

    @Column(name = "workouts_completed")
    @Builder.Default
    private Integer workoutsCompleted = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_workout_date")
    private LocalDateTime lastWorkoutDate;

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Workout> workouts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserAchievement> achievements = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calcula el badge del usuario según su nivel
     */
    @Transient
    public String getBadge() {
        if (currentLevel >= 50) return "LEGENDARY_HERO";
        if (currentLevel >= 40) return "MASTER_HERO";
        if (currentLevel >= 30) return "VETERAN_HERO";
        if (currentLevel >= 20) return "PROFESSIONAL_HERO";
        if (currentLevel >= 10) return "ADVANCED_HERO";
        if (currentLevel >= 5) return "INTERMEDIATE_HERO";
        return "BEGINNER_HERO";
    }

    /**
     * Calcula puntos necesarios para el siguiente nivel
     */
    @Transient
    public Integer getPointsForNextLevel() {
        if (currentLevel >= MAX_LEVEL) {
            return 0;
        }

        long required = (long) BASE_POINTS << (currentLevel - 1);
        if (required > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) required;
    }

    /**
     * Añade puntos y sube de nivel si es necesario
     */
    public void addPoints(Integer points) {
        if (points == null || points <= 0) {
            return;
        }

        this.totalPoints += points;

        // Calcular nuevo nivel con requisitos exponenciales hasta MAX_LEVEL
        int level = this.currentLevel;
        while (level < MAX_LEVEL && this.totalPoints >= cumulativePointsForLevel(level + 1)) {
            level++;
        }
        this.currentLevel = level;
    }

    private long cumulativePointsForLevel(int targetLevel) {
        // Puntos totales requeridos para alcanzar targetLevel (nivel 1 requiere 0)
        long total = 0;
        long required = BASE_POINTS;

        for (int lvl = 1; lvl < targetLevel && lvl < MAX_LEVEL + 1; lvl++) {
            total = Math.min(Long.MAX_VALUE, total + required);
            required = Math.min(Long.MAX_VALUE, required * 2);
        }

        return total;
    }
}
