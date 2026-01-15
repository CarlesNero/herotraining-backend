package com.base.base.login.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.base.base.login.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByKeycloakId(String keycloakId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByKeycloakId(String keycloakId);
    
    // Leaderboard - usuarios ordenados por puntos totales
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.totalPoints DESC, u.currentLevel DESC")
    List<User> findTopUsersByPoints(Pageable pageable);
    
    // Usuarios por nivel
    List<User> findByCurrentLevelOrderByTotalPointsDesc(Integer level);
}

