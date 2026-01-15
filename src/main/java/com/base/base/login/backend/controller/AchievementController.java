package com.base.base.login.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.base.base.login.backend.dto.AchievementDTO;
import com.base.base.login.backend.dto.AchievementRequest;
import com.base.base.login.backend.dto.UserAchievementDTO;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.service.AchievementService;
import com.base.base.login.backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestión de logros
 */
@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://herotraining.csanchezm.es"})
public class AchievementController {

    private final AchievementService achievementService;
    private final UserService userService;

    /**
     * GET /api/achievements - Obtiene todos los logros activos
     */
    @GetMapping
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements() {
        List<AchievementDTO> achievements = achievementService.getAllActiveAchievements();
        return ResponseEntity.ok(achievements);
    }

    /**
     * GET /api/achievements/my - Obtiene los logros del usuario actual
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<UserAchievementDTO>> getMyAchievements(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<UserAchievementDTO> achievements = achievementService.getUserAchievements(user);
        return ResponseEntity.ok(achievements);
    }

    /**
     * GET /api/achievements/{id} - Obtiene un logro por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<AchievementDTO> getAchievementById(@PathVariable Long id) {
        AchievementDTO achievement = achievementService.toDTO(achievementService.getAchievementById(id));
        return ResponseEntity.ok(achievement);
    }

    /**
     * POST /api/achievements - Crea un nuevo logro (solo admin)
     */
    @PostMapping
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<AchievementDTO> createAchievement(@RequestBody AchievementRequest request) {
        AchievementDTO created = achievementService.createAchievement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/achievements/{id} - Actualiza un logro (solo admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<AchievementDTO> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequest request) {
        AchievementDTO updated = achievementService.updateAchievement(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/achievements/{id} - Elimina un logro (solo admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
