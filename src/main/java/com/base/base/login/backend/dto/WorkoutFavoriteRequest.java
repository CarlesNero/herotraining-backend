package com.base.base.login.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para marcar o desmarcar una rutina como favorita
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutFavoriteRequest {
    private boolean favorite;
}
