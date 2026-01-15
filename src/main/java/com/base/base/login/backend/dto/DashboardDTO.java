package com.base.base.login.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para el dashboard del usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private UserDTO user;
    private DashboardStats stats;
    private List<WorkoutDTO> recentWorkouts;
    private List<UserAchievementDTO> recentAchievements;
    private List<WorkoutDTO> favoriteWorkouts;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DashboardStats {
        private Integer totalWorkouts;
        private Integer totalPoints;
        private Integer currentLevel;
        private String badge;
        private Integer pointsForNextLevel;
        private Integer achievementsUnlocked;
        private Integer workoutsThisWeek;
        private Integer workoutsThisMonth;
        private Double averageWorkoutPoints;
    }
}
