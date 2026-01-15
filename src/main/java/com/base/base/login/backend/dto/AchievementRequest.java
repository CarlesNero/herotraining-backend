package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Request para crear/editar logros
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementRequest {
    private String name;
    private String description;
    private String type; // WORKOUT_COUNT, TOTAL_POINTS, LEVEL_REACHED, etc.
    private String iconUrl;
    private Integer requiredValue;
    private Integer pointsReward;
    private String rarity; // COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
}
