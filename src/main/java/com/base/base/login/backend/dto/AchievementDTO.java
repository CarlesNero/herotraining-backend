package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para logros
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementDTO {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String iconUrl;
    private Integer requiredValue;
    private Integer pointsReward;
    private String rarity;
    private Boolean isActive;
}
