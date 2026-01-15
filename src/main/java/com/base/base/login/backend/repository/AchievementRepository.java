package com.base.base.login.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByName(String name);
    List<Achievement> findByIsActiveTrue();
    List<Achievement> findByType(Achievement.AchievementType type);
    List<Achievement> findByRarity(Achievement.AchievementRarity rarity);
    boolean existsByName(String name);
}
