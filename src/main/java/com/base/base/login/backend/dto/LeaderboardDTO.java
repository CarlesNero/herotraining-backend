package com.base.base.login.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para el leaderboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardDTO {
    private List<LeaderboardEntry> entries;
    private Integer totalUsers;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LeaderboardEntry {
        private Integer rank;
        private String username;
        private Integer totalPoints;
        private Integer currentLevel;
        private String badge;
        private Integer workoutsCompleted;
        private Integer achievementsCount;
    }
}
