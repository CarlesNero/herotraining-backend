package com.base.base.login.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.UserAchievement;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.entity.Achievement;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUserOrderByUnlockedAtDesc(User user);
    
    @Query("SELECT COUNT(ua) FROM UserAchievement ua WHERE ua.user = :user")
    Long countByUser(@Param("user") User user);
    
    boolean existsByUserAndAchievement(User user, Achievement achievement);
    
    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);
}
