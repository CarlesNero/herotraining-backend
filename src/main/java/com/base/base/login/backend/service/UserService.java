package com.base.base.login.backend.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.base.base.login.backend.dto.UserDTO;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.repository.UserAchievementRepository;
import com.base.base.login.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de gestión de usuarios
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;

    /**
     * Obtiene o crea un usuario desde el token JWT de Keycloak
     */
    @Transactional
    public User getOrCreateUserFromAuthentication(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        
        String keycloakId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        return userRepository.findByKeycloakId(keycloakId)
            .orElseGet(() -> {
                log.info("Creando nuevo usuario desde Keycloak: {}", username);
                User newUser = User.builder()
                    .keycloakId(keycloakId)
                    .username(username)
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .totalPoints(0)
                    .currentLevel(1)
                    .workoutsCompleted(0)
                    .isActive(true)
                    .build();
                return userRepository.save(newUser);
            });
    }

    /**
     * Convierte entidad User a DTO
     */
    public UserDTO toDTO(User user) {
        Long achievementsCount = userAchievementRepository.countByUser(user);
        
        return UserDTO.builder()
            .keycloakId(user.getKeycloakId())
            .username(user.getUsername())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .totalPoints(user.getTotalPoints())
            .currentLevel(user.getCurrentLevel())
            .workoutsCompleted(user.getWorkoutsCompleted())
            .badge(user.getBadge())
            .pointsForNextLevel(user.getPointsForNextLevel())
            .achievementsCount(achievementsCount.intValue())
            .build();
    }

    /**
     * Obtiene usuario actual
     */
    public User getCurrentUser(Authentication authentication) {
        return getOrCreateUserFromAuthentication(authentication);
    }

    /**
     * Actualiza información del usuario
     */
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
