package com.base.base.login.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.base.login.backend.dto.ExerciseDTO;
import com.base.base.login.backend.dto.ExerciseRequest;
import com.base.base.login.backend.entity.Exercise;
import com.base.base.login.backend.repository.ExerciseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestión de ejercicios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    /**
     * Obtiene todos los ejercicios activos
     */
    public List<ExerciseDTO> getAllActiveExercises() {
        return exerciseRepository.findByIsActiveTrue().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene un ejercicio por ID
     */
    public Exercise getExerciseById(Long id) {
        return exerciseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado con id: " + id));
    }

    /**
     * Crea un nuevo ejercicio
     */
    @Transactional
    public ExerciseDTO createExercise(ExerciseRequest request) {
        if (exerciseRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe un ejercicio con ese nombre");
        }

        Exercise exercise = Exercise.builder()
            .name(request.getName())
            .description(request.getDescription())
            .pointsReward(request.getPointsReward())
            .difficulty(Exercise.ExerciseDifficulty.valueOf(request.getDifficulty()))
            .category(Exercise.ExerciseCategory.valueOf(request.getCategory()))
            .estimatedDurationMinutes(request.getEstimatedDurationMinutes())
            .isActive(true)
            .build();

        Exercise saved = exerciseRepository.save(exercise);
        log.info("Ejercicio creado: {}", saved.getName());
        return toDTO(saved);
    }

    /**
     * Actualiza un ejercicio existente
     */
    @Transactional
    public ExerciseDTO updateExercise(Long id, ExerciseRequest request) {
        Exercise exercise = getExerciseById(id);

        if (!exercise.getName().equals(request.getName()) && 
            exerciseRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe otro ejercicio con ese nombre");
        }

        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setPointsReward(request.getPointsReward());
        exercise.setDifficulty(Exercise.ExerciseDifficulty.valueOf(request.getDifficulty()));
        exercise.setCategory(Exercise.ExerciseCategory.valueOf(request.getCategory()));
        exercise.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());

        Exercise updated = exerciseRepository.save(exercise);
        log.info("Ejercicio actualizado: {}", updated.getName());
        return toDTO(updated);
    }

    /**
     * Elimina (desactiva) un ejercicio
     */
    @Transactional
    public void deleteExercise(Long id) {
        Exercise exercise = getExerciseById(id);
        exercise.setIsActive(false);
        exerciseRepository.save(exercise);
        log.info("Ejercicio desactivado: {}", exercise.getName());
    }

    /**
     * Obtiene ejercicios por categoría
     */
    public List<ExerciseDTO> getExercisesByCategory(String category) {
        return exerciseRepository.findActiveByCategoryOrderByName(
            Exercise.ExerciseCategory.valueOf(category)
        ).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Convierte entidad a DTO
     */
    public ExerciseDTO toDTO(Exercise exercise) {
        return ExerciseDTO.builder()
            .id(exercise.getId())
            .name(exercise.getName())
            .description(exercise.getDescription())
            .pointsReward(exercise.getPointsReward())
            .difficulty(exercise.getDifficulty().name())
            .category(exercise.getCategory().name())
            .estimatedDurationMinutes(exercise.getEstimatedDurationMinutes())
            .isActive(exercise.getIsActive())
            .build();
    }
}
