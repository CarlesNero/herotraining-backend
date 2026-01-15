package com.base.base.login.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.base.login.backend.dto.ExerciseDTO;
import com.base.base.login.backend.dto.WorkoutDTO;
import com.base.base.login.backend.dto.WorkoutRequest;
import com.base.base.login.backend.entity.Exercise;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.entity.Workout;
import com.base.base.login.backend.entity.WorkoutExercise;
import com.base.base.login.backend.repository.WorkoutExerciseRepository;
import com.base.base.login.backend.repository.WorkoutRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestión de rutinas de entrenamiento
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseService exerciseService;
    private final AchievementService achievementService;

    /**
     * Obtiene todas las rutinas de un usuario
     */
    public List<WorkoutDTO> getUserWorkouts(User user) {
        return workoutRepository.findByUserOrderByCreatedAtDesc(user).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene rutinas completadas de un usuario
     */
    public List<WorkoutDTO> getCompletedWorkouts(User user) {
        return workoutRepository.findCompletedWorkoutsByUser(user).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene una rutina por ID
     */
    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rutina no encontrada con id: " + id));
    }

    /**
     * Crea una nueva rutina
     */
    @Transactional
    public WorkoutDTO createWorkout(User user, WorkoutRequest request) {
        Workout workout = Workout.builder()
            .name(request.getName())
            .description(request.getDescription())
            .user(user)
            .status(Workout.WorkoutStatus.PLANNED)
            .totalPoints(0)
            .exercises(new ArrayList<>())
            .favorite(Boolean.TRUE.equals(request.getFavorite()))
            .build();

        Workout savedWorkout = workoutRepository.save(workout);

        // Añadir ejercicios
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            for (WorkoutRequest.WorkoutExerciseRequest exReq : request.getExercises()) {
                Exercise exercise = exerciseService.getExerciseById(exReq.getExerciseId());
                
                WorkoutExercise workoutExercise = WorkoutExercise.builder()
                    .workout(savedWorkout)
                    .exercise(exercise)
                    .sets(exReq.getSets())
                    .reps(exReq.getReps())
                    .restSeconds(exReq.getRestSeconds())
                    .orderIndex(exReq.getOrderIndex())
                    .notes(exReq.getNotes())
                    .isCompleted(false)
                    .build();

                savedWorkout.getExercises().add(workoutExerciseRepository.save(workoutExercise));
            }
        }

        log.info("Rutina creada: {} para usuario: {}", savedWorkout.getName(), user.getUsername());
        return toDTO(savedWorkout);
    }

    /**
     * Actualiza una rutina existente
     */
    @Transactional
    public WorkoutDTO updateWorkout(Long id, User user, WorkoutRequest request) {
        Workout workout = getWorkoutById(id);

        if (!workout.getUser().getKeycloakId().equals(user.getKeycloakId())) {
            throw new RuntimeException("No tienes permiso para editar esta rutina");
        }

        if (workout.getStatus() == Workout.WorkoutStatus.COMPLETED) {
            throw new RuntimeException("No se puede editar una rutina completada");
        }

        workout.setName(request.getName());
        workout.setDescription(request.getDescription());

        if (request.getFavorite() != null) {
            workout.setFavorite(request.getFavorite());
        }

        // Eliminar ejercicios existentes
        workoutExerciseRepository.deleteByWorkout(workout);
        workout.getExercises().clear();

        // Añadir nuevos ejercicios
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            for (WorkoutRequest.WorkoutExerciseRequest exReq : request.getExercises()) {
                Exercise exercise = exerciseService.getExerciseById(exReq.getExerciseId());
                
                WorkoutExercise workoutExercise = WorkoutExercise.builder()
                    .workout(workout)
                    .exercise(exercise)
                    .sets(exReq.getSets())
                    .reps(exReq.getReps())
                    .restSeconds(exReq.getRestSeconds())
                    .orderIndex(exReq.getOrderIndex())
                    .notes(exReq.getNotes())
                    .isCompleted(false)
                    .build();

                workout.getExercises().add(workoutExerciseRepository.save(workoutExercise));
            }
        }

        Workout updated = workoutRepository.save(workout);
        log.info("Rutina actualizada: {}", updated.getName());
        return toDTO(updated);
    }

    /**
     * Obtiene las rutinas favoritas del usuario
     */
    public List<WorkoutDTO> getFavoriteWorkouts(User user) {
        return workoutRepository.findByUserAndFavoriteTrueOrderByUpdatedAtDesc(user).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de favorito de una rutina
     */
    @Transactional
    public WorkoutDTO updateFavoriteStatus(Long id, User user, boolean favorite) {
        Workout workout = getWorkoutById(id);

        if (!workout.getUser().getKeycloakId().equals(user.getKeycloakId())) {
            throw new RuntimeException("No tienes permiso para actualizar esta rutina");
        }

        workout.setFavorite(favorite);
        Workout saved = workoutRepository.save(workout);
        return toDTO(saved);
    }

    /**
     * Marca una rutina como completada
     */
    @Transactional
    public WorkoutDTO completeWorkout(Long id, User user) {
        Workout workout = getWorkoutById(id);

        if (!workout.getUser().getKeycloakId().equals(user.getKeycloakId())) {
            throw new RuntimeException("No tienes permiso para completar esta rutina");
        }

        // Evitar completar más de una vez por día
        if (workout.getCompletedAt() != null && workout.getStatus() == Workout.WorkoutStatus.COMPLETED) {
            boolean completedToday = workout.getCompletedAt().toLocalDate().isEqual(LocalDate.now());
            if (completedToday) {
                throw new RuntimeException("Esta rutina ya se completó hoy. Inténtalo mañana.");
            }
            // Si fue completada en días anteriores, resetear para permitir nueva sesión hoy
            workout.setStatus(Workout.WorkoutStatus.PLANNED);
            workout.setCompletedAt(null);
        }

        // Completar rutina
        workout.complete();
        
        // Actualizar estadísticas del usuario
        user.setWorkoutsCompleted(user.getWorkoutsCompleted() + 1);
        user.addPoints(workout.getTotalPoints());
        user.setLastWorkoutDate(LocalDateTime.now());

        workoutRepository.save(workout);

        // Verificar logros
        achievementService.checkAndUnlockAchievements(user);

        log.info("Rutina completada: {} - Puntos: {}", workout.getName(), workout.getTotalPoints());
        return toDTO(workout);
    }

    /**
     * Elimina una rutina
     */
    @Transactional
    public void deleteWorkout(Long id, User user) {
        Workout workout = getWorkoutById(id);

        if (!workout.getUser().getKeycloakId().equals(user.getKeycloakId())) {
            throw new RuntimeException("No tienes permiso para eliminar esta rutina");
        }

        if (workout.getStatus() == Workout.WorkoutStatus.COMPLETED) {
            throw new RuntimeException("No se puede eliminar una rutina completada");
        }

        workoutRepository.delete(workout);
        log.info("Rutina eliminada: {}", workout.getName());
    }

    /**
     * Convierte entidad a DTO
     */
    public WorkoutDTO toDTO(Workout workout) {
        List<WorkoutDTO.WorkoutExerciseDTO> exerciseDTOs = workout.getExercises().stream()
            .map(we -> {
                ExerciseDTO exerciseDTO = exerciseService.toDTO(we.getExercise());
                return WorkoutDTO.WorkoutExerciseDTO.builder()
                    .id(we.getId())
                    .exercise(exerciseDTO)
                    .sets(we.getSets())
                    .reps(we.getReps())
                    .restSeconds(we.getRestSeconds())
                    .orderIndex(we.getOrderIndex())
                    .notes(we.getNotes())
                    .isCompleted(we.getIsCompleted())
                    .build();
            })
            .collect(Collectors.toList());

        return WorkoutDTO.builder()
            .id(workout.getId())
            .name(workout.getName())
            .description(workout.getDescription())
            .status(workout.getStatus().name())
            .totalPoints(workout.getTotalPoints())
            .completedAt(workout.getCompletedAt())
            .createdAt(workout.getCreatedAt())
            .favorite(workout.isFavorite())
            .exercises(exerciseDTOs)
            .build();
    }
}
