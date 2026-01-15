package com.base.base.login.backend.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Achievement - Logros que pueden conseguir los usuarios
 */
@Entity
@Table(name = "achievements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AchievementType type = AchievementType.WORKOUT_COUNT;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "required_value")
    private Integer requiredValue;

    @Column(name = "points_reward")
    @Builder.Default
    private Integer pointsReward = 50;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AchievementRarity rarity = AchievementRarity.COMMON;

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

    public enum AchievementType {
        WORKOUT_COUNT,      // Por número de workouts completados
        TOTAL_POINTS,       // Por puntos totales acumulados
        LEVEL_REACHED,      // Por alcanzar un nivel
        CONSECUTIVE_DAYS,   // Por días consecutivos entrenando
        SPECIFIC_EXERCISE,  // Por completar un ejercicio específico
        CATEGORY_MASTER     // Por completar X workouts de una categoría
    }

    public enum AchievementRarity {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY
    }
}
