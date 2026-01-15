package com.base.base.login.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.base.base.login.backend.entity.Achievement;
import com.base.base.login.backend.entity.Exercise;
import com.base.base.login.backend.repository.AchievementRepository;
import com.base.base.login.backend.repository.ExerciseRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuración para inicializar datos de ejemplo
 */
@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ExerciseRepository exerciseRepository,
            AchievementRepository achievementRepository) {
        return args -> {
            // Solo inicializar si la base de datos está vacía
            if (exerciseRepository.count() == 0) {
                log.info("Inicializando ejercicios de ejemplo...");
                
                // Ejercicios de Cardio
                exerciseRepository.save(Exercise.builder()
                    .name("Correr 5km")
                    .description("Carrera continua de 5 kilómetros")
                    .pointsReward(50)
                    .difficulty(Exercise.ExerciseDifficulty.MEDIUM)
                    .category(Exercise.ExerciseCategory.CARDIO)
                    .estimatedDurationMinutes(30)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Burpees")
                    .description("Ejercicio completo de cuerpo")
                    .pointsReward(15)
                    .difficulty(Exercise.ExerciseDifficulty.HARD)
                    .category(Exercise.ExerciseCategory.HIIT)
                    .estimatedDurationMinutes(10)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Saltar la cuerda")
                    .description("Cardio de alta intensidad")
                    .pointsReward(20)
                    .difficulty(Exercise.ExerciseDifficulty.MEDIUM)
                    .category(Exercise.ExerciseCategory.CARDIO)
                    .estimatedDurationMinutes(15)
                    .isActive(true)
                    .build());

                // Ejercicios de Fuerza
                exerciseRepository.save(Exercise.builder()
                    .name("Flexiones")
                    .description("Push-ups clásicos")
                    .pointsReward(10)
                    .difficulty(Exercise.ExerciseDifficulty.EASY)
                    .category(Exercise.ExerciseCategory.STRENGTH)
                    .estimatedDurationMinutes(5)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Dominadas")
                    .description("Pull-ups en barra")
                    .pointsReward(20)
                    .difficulty(Exercise.ExerciseDifficulty.HARD)
                    .category(Exercise.ExerciseCategory.STRENGTH)
                    .estimatedDurationMinutes(10)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Sentadillas")
                    .description("Squats profundos")
                    .pointsReward(12)
                    .difficulty(Exercise.ExerciseDifficulty.MEDIUM)
                    .category(Exercise.ExerciseCategory.STRENGTH)
                    .estimatedDurationMinutes(10)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Plancha")
                    .description("Isométrico de core")
                    .pointsReward(8)
                    .difficulty(Exercise.ExerciseDifficulty.EASY)
                    .category(Exercise.ExerciseCategory.STRENGTH)
                    .estimatedDurationMinutes(5)
                    .isActive(true)
                    .build());

                // Ejercicios de Flexibilidad
                exerciseRepository.save(Exercise.builder()
                    .name("Yoga Vinyasa")
                    .description("Flujo dinámico de yoga")
                    .pointsReward(30)
                    .difficulty(Exercise.ExerciseDifficulty.MEDIUM)
                    .category(Exercise.ExerciseCategory.YOGA)
                    .estimatedDurationMinutes(45)
                    .isActive(true)
                    .build());

                exerciseRepository.save(Exercise.builder()
                    .name("Estiramientos")
                    .description("Rutina completa de estiramientos")
                    .pointsReward(10)
                    .difficulty(Exercise.ExerciseDifficulty.VERY_EASY)
                    .category(Exercise.ExerciseCategory.FLEXIBILITY)
                    .estimatedDurationMinutes(15)
                    .isActive(true)
                    .build());

                // CrossFit
                exerciseRepository.save(Exercise.builder()
                    .name("WOD Cindy")
                    .description("5 Pull-ups, 10 Push-ups, 15 Squats - AMRAP 20min")
                    .pointsReward(60)
                    .difficulty(Exercise.ExerciseDifficulty.VERY_HARD)
                    .category(Exercise.ExerciseCategory.CROSSFIT)
                    .estimatedDurationMinutes(20)
                    .isActive(true)
                    .build());

                log.info("✅ {} ejercicios creados", exerciseRepository.count());
            }

            if (achievementRepository.count() == 0) {
                log.info("Inicializando logros de ejemplo...");

                // Logros por número de entrenamientos
                achievementRepository.save(Achievement.builder()
                    .name("Primera Victoria")
                    .description("Completa tu primer entrenamiento")
                    .type(Achievement.AchievementType.WORKOUT_COUNT)
                    .requiredValue(1)
                    .pointsReward(50)
                    .rarity(Achievement.AchievementRarity.COMMON)
                    .iconUrl("🎉")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Constancia")
                    .description("Completa 10 entrenamientos")
                    .type(Achievement.AchievementType.WORKOUT_COUNT)
                    .requiredValue(10)
                    .pointsReward(100)
                    .rarity(Achievement.AchievementRarity.UNCOMMON)
                    .iconUrl("💪")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Guerrero")
                    .description("Completa 50 entrenamientos")
                    .type(Achievement.AchievementType.WORKOUT_COUNT)
                    .requiredValue(50)
                    .pointsReward(300)
                    .rarity(Achievement.AchievementRarity.RARE)
                    .iconUrl("⚔️")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Leyenda")
                    .description("Completa 100 entrenamientos")
                    .type(Achievement.AchievementType.WORKOUT_COUNT)
                    .requiredValue(100)
                    .pointsReward(500)
                    .rarity(Achievement.AchievementRarity.EPIC)
                    .iconUrl("👑")
                    .isActive(true)
                    .build());

                // Logros por puntos
                achievementRepository.save(Achievement.builder()
                    .name("Centurión")
                    .description("Alcanza 100 puntos")
                    .type(Achievement.AchievementType.TOTAL_POINTS)
                    .requiredValue(100)
                    .pointsReward(50)
                    .rarity(Achievement.AchievementRarity.COMMON)
                    .iconUrl("💯")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Millar")
                    .description("Alcanza 1000 puntos")
                    .type(Achievement.AchievementType.TOTAL_POINTS)
                    .requiredValue(1000)
                    .pointsReward(200)
                    .rarity(Achievement.AchievementRarity.RARE)
                    .iconUrl("🌟")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Imparable")
                    .description("Alcanza 5000 puntos")
                    .type(Achievement.AchievementType.TOTAL_POINTS)
                    .requiredValue(5000)
                    .pointsReward(500)
                    .rarity(Achievement.AchievementRarity.LEGENDARY)
                    .iconUrl("🔥")
                    .isActive(true)
                    .build());

                // Logros por nivel
                achievementRepository.save(Achievement.builder()
                    .name("Héroe Novato")
                    .description("Alcanza el nivel 5")
                    .type(Achievement.AchievementType.LEVEL_REACHED)
                    .requiredValue(5)
                    .pointsReward(100)
                    .rarity(Achievement.AchievementRarity.COMMON)
                    .iconUrl("🎓")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Héroe Experto")
                    .description("Alcanza el nivel 20")
                    .type(Achievement.AchievementType.LEVEL_REACHED)
                    .requiredValue(20)
                    .pointsReward(300)
                    .rarity(Achievement.AchievementRarity.EPIC)
                    .iconUrl("🏅")
                    .isActive(true)
                    .build());

                achievementRepository.save(Achievement.builder()
                    .name("Héroe Supremo")
                    .description("Alcanza el nivel 50")
                    .type(Achievement.AchievementType.LEVEL_REACHED)
                    .requiredValue(50)
                    .pointsReward(1000)
                    .rarity(Achievement.AchievementRarity.LEGENDARY)
                    .iconUrl("👑")
                    .isActive(true)
                    .build());

                log.info("✅ {} logros creados", achievementRepository.count());
            }

            log.info("🚀 Datos iniciales cargados correctamente");
        };
    }
}
