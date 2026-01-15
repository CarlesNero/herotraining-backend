package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para información del usuario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String keycloakId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Integer totalPoints;
    private Integer currentLevel;
    private Integer workoutsCompleted;
    private String badge;
    private Integer pointsForNextLevel;
    private Integer achievementsCount;
}
