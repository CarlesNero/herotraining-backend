package com.base.base.login.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.base.login.backend.dto.AchievementDTO;
import com.base.base.login.backend.dto.AchievementRequest;
import com.base.base.login.backend.dto.UserAchievementDTO;
import com.base.base.login.backend.entity.Achievement;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.entity.UserAchievement;
import com.base.base.login.backend.repository.AchievementRepository;
import com.base.base.login.backend.repository.UserAchievementRepository;
import com.base.base.login.backend.repository.WorkoutExerciseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestión de logros
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    /**
     * Obtiene todos los logros activos
     */
    public List<AchievementDTO> getAllActiveAchievements() {
        return achievementRepository.findByIsActiveTrue().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene los logros de un usuario
     */
    public List<UserAchievementDTO> getUserAchievements(User user) {
        return userAchievementRepository.findByUserOrderByUnlockedAtDesc(user).stream()
            .map(this::toUserAchievementDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene un logro por ID
     */
    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Logro no encontrado con id: " + id));
    }

    /**
     * Crea un nuevo logro
     */
    @Transactional
    public AchievementDTO createAchievement(AchievementRequest request) {
        if (achievementRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe un logro con ese nombre");
        }

        Achievement achievement = Achievement.builder()
            .name(request.getName())
            .description(request.getDescription())
            .type(Achievement.AchievementType.valueOf(request.getType()))
            .iconUrl(request.getIconUrl())
            .requiredValue(request.getRequiredValue())
            .pointsReward(request.getPointsReward())
            .rarity(Achievement.AchievementRarity.valueOf(request.getRarity()))
            .isActive(true)
            .build();

        Achievement saved = achievementRepository.save(achievement);
        log.info("Logro creado: {}", saved.getName());
        return toDTO(saved);
    }

    /**
     * Actualiza un logro existente
     */
    @Transactional
    public AchievementDTO updateAchievement(Long id, AchievementRequest request) {
        Achievement achievement = getAchievementById(id);

        if (!achievement.getName().equals(request.getName()) && 
            achievementRepository.existsByName(request.getName())) {
            throw new RuntimeException("Ya existe otro logro con ese nombre");
        }

        achievement.setName(request.getName());
        achievement.setDescription(request.getDescription());
        achievement.setType(Achievement.AchievementType.valueOf(request.getType()));
        achievement.setIconUrl(request.getIconUrl());
        achievement.setRequiredValue(request.getRequiredValue());
        achievement.setPointsReward(request.getPointsReward());
        achievement.setRarity(Achievement.AchievementRarity.valueOf(request.getRarity()));

        Achievement updated = achievementRepository.save(achievement);
        log.info("Logro actualizado: {}", updated.getName());
        return toDTO(updated);
    }

    /**
     * Elimina (desactiva) un logro
     */
    @Transactional
    public void deleteAchievement(Long id) {
        Achievement achievement = getAchievementById(id);
        achievement.setIsActive(false);
        achievementRepository.save(achievement);
        log.info("Logro desactivado: {}", achievement.getName());
    }

    /**
     * Desbloquea un logro para un usuario
     */
    @Transactional
    public UserAchievementDTO unlockAchievement(User user, Achievement achievement) {
        if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) {
            throw new RuntimeException("El usuario ya tiene este logro");
        }

        UserAchievement userAchievement = UserAchievement.builder()
            .user(user)
            .achievement(achievement)
            .build();

        UserAchievement saved = userAchievementRepository.save(userAchievement);
        
        // Añadir puntos de recompensa
        user.addPoints(achievement.getPointsReward());
        
        log.info("Logro desbloqueado: {} para usuario: {}", achievement.getName(), user.getUsername());
        return toUserAchievementDTO(saved);
    }

    /**
     * Verifica y desbloquea logros automáticamente según el progreso del usuario
     */
    @Transactional
    public void checkAndUnlockAchievements(User user) {
        List<Achievement> activeAchievements = achievementRepository.findByIsActiveTrue();

        for (Achievement achievement : activeAchievements) {
            // Si ya tiene el logro, continuar
            if (userAchievementRepository.existsByUserAndAchievement(user, achievement)) {
                continue;
            }

            boolean shouldUnlock = false;

            switch (achievement.getType()) {
                case WORKOUT_COUNT:
                    shouldUnlock = user.getWorkoutsCompleted() >= achievement.getRequiredValue();
                    break;
                case TOTAL_POINTS:
                    shouldUnlock = user.getTotalPoints() >= achievement.getRequiredValue();
                    break;
                case LEVEL_REACHED:
                    shouldUnlock = user.getCurrentLevel() >= achievement.getRequiredValue();
                    break;
                case CATEGORY_MASTER:
                    Long distinctExercises = workoutExerciseRepository.countDistinctCompletedExercisesByUser(user);
                    shouldUnlock = distinctExercises != null && achievement.getRequiredValue() != null
                        && distinctExercises >= achievement.getRequiredValue();
                    break;
                default:
                    // Otros tipos requieren verificación manual
                    break;
            }

            if (shouldUnlock) {
                unlockAchievement(user, achievement);
            }
        }
    }

    /**
     * Convierte Achievement a DTO
     */
    public AchievementDTO toDTO(Achievement achievement) {
        return AchievementDTO.builder()
            .id(achievement.getId())
            .name(achievement.getName())
            .description(achievement.getDescription())
            .type(achievement.getType().name())
            .iconUrl(achievement.getIconUrl())
            .requiredValue(achievement.getRequiredValue())
            .pointsReward(achievement.getPointsReward())
            .rarity(achievement.getRarity().name())
            .isActive(achievement.getIsActive())
            .build();
    }

    /**
     * Convierte UserAchievement a DTO
     */
    public UserAchievementDTO toUserAchievementDTO(UserAchievement userAchievement) {
        return UserAchievementDTO.builder()
            .id(userAchievement.getId())
            .achievement(toDTO(userAchievement.getAchievement()))
            .unlockedAt(userAchievement.getUnlockedAt())
            .progressValue(userAchievement.getProgressValue())
            .build();
    }
}
