package com.base.base.login.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.base.login.backend.dto.WorkoutDTO;
import com.base.base.login.backend.dto.WorkoutFavoriteRequest;
import com.base.base.login.backend.dto.WorkoutRequest;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.service.UserService;
import com.base.base.login.backend.service.WorkoutService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestión de rutinas de entrenamiento
 */
@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://herotraining.csanchezm.es"})
public class WorkoutController {

    private final WorkoutService workoutService;
    private final UserService userService;

    /**
     * GET /api/workouts - Obtiene todas las rutinas del usuario
     */
    @GetMapping
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<WorkoutDTO>> getUserWorkouts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<WorkoutDTO> workouts = workoutService.getUserWorkouts(user);
        return ResponseEntity.ok(workouts);
    }

    /**
     * GET /api/workouts/favorites - Obtiene las rutinas favoritas del usuario
     */
    @GetMapping("/favorites")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<WorkoutDTO>> getFavoriteWorkouts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<WorkoutDTO> workouts = workoutService.getFavoriteWorkouts(user);
        return ResponseEntity.ok(workouts);
    }

    /**
     * GET /api/workouts/completed - Obtiene rutinas completadas
     */
    @GetMapping("/completed")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<WorkoutDTO>> getCompletedWorkouts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        List<WorkoutDTO> workouts = workoutService.getCompletedWorkouts(user);
        return ResponseEntity.ok(workouts);
    }

    /**
     * GET /api/workouts/{id} - Obtiene una rutina por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<WorkoutDTO> getWorkoutById(@PathVariable Long id) {
        WorkoutDTO workout = workoutService.toDTO(workoutService.getWorkoutById(id));
        return ResponseEntity.ok(workout);
    }

    /**
     * POST /api/workouts - Crea una nueva rutina
     */
    @PostMapping
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<WorkoutDTO> createWorkout(
            @RequestBody WorkoutRequest request,
            Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        WorkoutDTO created = workoutService.createWorkout(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/workouts/{id} - Actualiza una rutina
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<WorkoutDTO> updateWorkout(
            @PathVariable Long id,
            @RequestBody WorkoutRequest request,
            Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        WorkoutDTO updated = workoutService.updateWorkout(id, user, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * POST /api/workouts/{id}/complete - Marca una rutina como completada
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<WorkoutDTO> completeWorkout(
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        WorkoutDTO completed = workoutService.completeWorkout(id, user);
        return ResponseEntity.ok(completed);
    }

    /**
     * POST /api/workouts/{id}/favorite - Actualiza el estado de favorito de una rutina
     */
    @PostMapping("/{id}/favorite")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<WorkoutDTO> updateFavorite(
            @PathVariable Long id,
            @RequestBody WorkoutFavoriteRequest request,
            Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        WorkoutDTO updated = workoutService.updateFavoriteStatus(id, user, request.isFavorite());
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/workouts/{id} - Elimina una rutina
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long id,
            Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        workoutService.deleteWorkout(id, user);
        return ResponseEntity.noContent().build();
    }
}
