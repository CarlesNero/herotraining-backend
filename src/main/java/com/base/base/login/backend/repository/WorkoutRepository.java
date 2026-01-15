package com.base.base.login.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.Workout;
import com.base.base.login.backend.entity.User;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserOrderByCreatedAtDesc(User user);
    List<Workout> findByUserAndStatusOrderByCreatedAtDesc(User user, Workout.WorkoutStatus status);
    
    @Query("SELECT w FROM Workout w WHERE w.user = :user AND w.status = 'COMPLETED' ORDER BY w.completedAt DESC")
    List<Workout> findCompletedWorkoutsByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user = :user AND w.status = 'COMPLETED'")
    Long countCompletedWorkoutsByUser(@Param("user") User user);
    
    @Query("SELECT w FROM Workout w WHERE w.user = :user AND w.completedAt BETWEEN :start AND :end")
    List<Workout> findWorkoutsByUserAndDateRange(
        @Param("user") User user, 
        @Param("start") LocalDateTime start, 
        @Param("end") LocalDateTime end
    );
    
    @Query("SELECT SUM(w.totalPoints) FROM Workout w WHERE w.user = :user AND w.status = 'COMPLETED'")
    Integer getTotalPointsByUser(@Param("user") User user);

    List<Workout> findByUserAndFavoriteTrueOrderByUpdatedAtDesc(User user);

    List<Workout> findTop5ByUserAndFavoriteTrueOrderByUpdatedAtDesc(User user);
}
