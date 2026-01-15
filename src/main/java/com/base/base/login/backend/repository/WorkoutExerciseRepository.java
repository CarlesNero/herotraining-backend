package com.base.base.login.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.WorkoutExercise;
import com.base.base.login.backend.entity.Workout;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
    List<WorkoutExercise> findByWorkoutOrderByOrderIndexAsc(Workout workout);
    void deleteByWorkout(Workout workout);

    @Query("SELECT COUNT(DISTINCT we.exercise.id) FROM WorkoutExercise we WHERE we.workout.user = :user AND we.workout.status = 'COMPLETED'")
    Long countDistinctCompletedExercisesByUser(@Param("user") com.base.base.login.backend.entity.User user);
}
