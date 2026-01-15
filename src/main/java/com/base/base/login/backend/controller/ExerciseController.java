package com.base.base.login.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.base.base.login.backend.dto.ExerciseDTO;
import com.base.base.login.backend.dto.ExerciseRequest;
import com.base.base.login.backend.service.ExerciseService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para gestión de ejercicios
 */
@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://herotraining.csanchezm.es"})
public class ExerciseController {

    private final ExerciseService exerciseService;

    /**
     * GET /api/exercises - Obtiene todos los ejercicios activos
     */
    @GetMapping
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllActiveExercises();
        return ResponseEntity.ok(exercises);
    }

    /**
     * GET /api/exercises/{id} - Obtiene un ejercicio por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Long id) {
        ExerciseDTO exercise = exerciseService.toDTO(exerciseService.getExerciseById(id));
        return ResponseEntity.ok(exercise);
    }

    /**
     * GET /api/exercises/category/{category} - Obtiene ejercicios por categoría
     */
    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<List<ExerciseDTO>> getExercisesByCategory(@PathVariable String category) {
        List<ExerciseDTO> exercises = exerciseService.getExercisesByCategory(category);
        return ResponseEntity.ok(exercises);
    }

    /**
     * POST /api/exercises - Crea un nuevo ejercicio (solo admin)
     */
    @PostMapping
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseRequest request) {
        ExerciseDTO created = exerciseService.createExercise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/exercises/{id} - Actualiza un ejercicio (solo admin)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<ExerciseDTO> updateExercise(
            @PathVariable Long id,
            @RequestBody ExerciseRequest request) {
        ExerciseDTO updated = exerciseService.updateExercise(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/exercises/{id} - Elimina un ejercicio (solo admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('nero-admin')")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
