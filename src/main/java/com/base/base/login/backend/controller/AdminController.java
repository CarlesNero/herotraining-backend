package com.base.base.login.backend.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.base.base.login.backend.dto.AchievementDTO;
import com.base.base.login.backend.dto.ExerciseDTO;
import com.base.base.login.backend.dto.LeaderboardDTO;
import com.base.base.login.backend.dto.UserDTO;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.repository.UserRepository;
import com.base.base.login.backend.service.AchievementService;
import com.base.base.login.backend.service.DashboardService;
import com.base.base.login.backend.service.ExerciseService;
import com.base.base.login.backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para el panel de administrador
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://herotraining.csanchezm.es"})
public class AdminController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;
    private final AchievementService achievementService;

    /**
     * GET /api/admin/leaderboard - Obtiene el leaderboard
     */
    @GetMapping("/leaderboard")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<LeaderboardDTO> getLeaderboard(
            @RequestParam(defaultValue = "50") int limit) {
        LeaderboardDTO leaderboard = dashboardService.getLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * GET /api/admin/users - Obtiene todos los usuarios
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        List<UserDTO> users = userRepository.findAll(PageRequest.of(page, size))
            .getContent()
            .stream()
            .map(userService::toDTO)
            .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/admin/users/{keycloakId} - Obtiene un usuario específico
     */
    @GetMapping("/users/{keycloakId}")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(userService.toDTO(user));
    }

    /**
     * GET /api/admin/exercises - Obtiene todos los ejercicios (incluidos inactivos)
     */
    @GetMapping("/exercises")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllActiveExercises();
        return ResponseEntity.ok(exercises);
    }

    /**
     * GET /api/admin/achievements - Obtiene todos los logros (incluidos inactivos)
     */
    @GetMapping("/achievements")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        List<AchievementDTO> achievements = achievementService.getAllActiveAchievements();
        return ResponseEntity.ok(achievements);
    }
}
