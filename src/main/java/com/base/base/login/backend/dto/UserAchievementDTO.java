package com.base.base.login.backend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO Response para logros de usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAchievementDTO {
    private Long id;
    private AchievementDTO achievement;
    private LocalDateTime unlockedAt;
    private Integer progressValue;
}
