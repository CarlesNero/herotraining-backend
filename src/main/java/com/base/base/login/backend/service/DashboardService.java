package com.base.base.login.backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.base.base.login.backend.dto.DashboardDTO;
import com.base.base.login.backend.dto.LeaderboardDTO;
import com.base.base.login.backend.dto.UserAchievementDTO;
import com.base.base.login.backend.dto.WorkoutDTO;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.repository.UserRepository;
import com.base.base.login.backend.repository.WorkoutRepository;
import com.base.base.login.backend.repository.UserAchievementRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de dashboard y leaderboard
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final WorkoutService workoutService;
    private final AchievementService achievementService;
    private final UserService userService;

    /**
     * Obtiene los datos del dashboard del usuario
     */
    public DashboardDTO getDashboard(User user) {
        // Estadísticas
        Long totalWorkouts = workoutRepository.countCompletedWorkoutsByUser(user);
        Integer totalPoints = user.getTotalPoints();
        Long achievementsCount = userAchievementRepository.countByUser(user);

        // Workouts de esta semana
        LocalDateTime weekStart = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        LocalDateTime now = LocalDateTime.now();
        List<com.base.base.login.backend.entity.Workout> workoutsThisWeek = 
            workoutRepository.findWorkoutsByUserAndDateRange(user, weekStart, now);

        // Workouts de este mes
        LocalDateTime monthStart = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
        List<com.base.base.login.backend.entity.Workout> workoutsThisMonth = 
            workoutRepository.findWorkoutsByUserAndDateRange(user, monthStart, now);

        // Promedio de puntos por workout
        Double averagePoints = totalWorkouts > 0 ? 
            totalPoints.doubleValue() / totalWorkouts.doubleValue() : 0.0;

        DashboardDTO.DashboardStats stats = DashboardDTO.DashboardStats.builder()
            .totalWorkouts(totalWorkouts.intValue())
            .totalPoints(totalPoints)
            .currentLevel(user.getCurrentLevel())
            .badge(user.getBadge())
            .pointsForNextLevel(user.getPointsForNextLevel())
            .achievementsUnlocked(achievementsCount.intValue())
            .workoutsThisWeek(workoutsThisWeek.size())
            .workoutsThisMonth(workoutsThisMonth.size())
            .averageWorkoutPoints(averagePoints)
            .build();

        // Últimas 5 rutinas completadas
        List<WorkoutDTO> recentWorkouts = workoutRepository
            .findCompletedWorkoutsByUser(user).stream()
            .limit(5)
            .map(workoutService::toDTO)
            .collect(Collectors.toList());

        // Rutinas favoritas (máximo 5)
        List<WorkoutDTO> favoriteWorkouts = workoutRepository
            .findTop5ByUserAndFavoriteTrueOrderByUpdatedAtDesc(user).stream()
            .map(workoutService::toDTO)
            .collect(Collectors.toList());

        // Últimos 5 logros desbloqueados
        List<UserAchievementDTO> recentAchievements = userAchievementRepository
            .findByUserOrderByUnlockedAtDesc(user).stream()
            .limit(5)
            .map(achievementService::toUserAchievementDTO)
            .collect(Collectors.toList());

        return DashboardDTO.builder()
            .user(userService.toDTO(user))
            .stats(stats)
            .recentWorkouts(recentWorkouts)
            .recentAchievements(recentAchievements)
                .favoriteWorkouts(favoriteWorkouts)
            .build();
    }

    /**
     * Obtiene el leaderboard (top usuarios por puntos)
     */
    public LeaderboardDTO getLeaderboard(int limit) {
        List<User> topUsers = userRepository.findTopUsersByPoints(PageRequest.of(0, limit));
        
        List<LeaderboardDTO.LeaderboardEntry> entries = topUsers.stream()
            .map(user -> {
                int rank = topUsers.indexOf(user) + 1;
                Long achievementsCount = userAchievementRepository.countByUser(user);
                
                return LeaderboardDTO.LeaderboardEntry.builder()
                    .rank(rank)
                    .username(user.getUsername())
                    .totalPoints(user.getTotalPoints())
                    .currentLevel(user.getCurrentLevel())
                    .badge(user.getBadge())
                    .workoutsCompleted(user.getWorkoutsCompleted())
                    .achievementsCount(achievementsCount.intValue())
                    .build();
            })
            .collect(Collectors.toList());

        return LeaderboardDTO.builder()
            .entries(entries)
            .totalUsers(entries.size())
            .build();
    }
}
