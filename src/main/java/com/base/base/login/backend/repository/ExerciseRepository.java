package com.base.base.login.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.Exercise;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);
    List<Exercise> findByIsActiveTrue();
    List<Exercise> findByDifficulty(Exercise.ExerciseDifficulty difficulty);
    List<Exercise> findByCategory(Exercise.ExerciseCategory category);
    
    @Query("SELECT e FROM Exercise e WHERE e.isActive = true AND e.category = :category")
    List<Exercise> findActiveByCategoryOrderByName(Exercise.ExerciseCategory category);
    
    boolean existsByName(String name);
}
