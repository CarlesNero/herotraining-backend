package com.base.base.login.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.base.base.login.backend.dto.DashboardDTO;
import com.base.base.login.backend.dto.UserDTO;
import com.base.base.login.backend.entity.User;
import com.base.base.login.backend.service.DashboardService;
import com.base.base.login.backend.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para dashboard y perfil de usuario
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201", "https://herotraining.csanchezm.es"})
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    /**
     * GET /api/dashboard - Obtiene el dashboard completo del usuario
     */
    @GetMapping
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<DashboardDTO> getDashboard(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        DashboardDTO dashboard = dashboardService.getDashboard(user);
        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/dashboard/profile - Obtiene el perfil del usuario actual
     */
    @GetMapping("/profile")
    @PreAuthorize("hasRole('default-roles-neroapps')")
    public ResponseEntity<UserDTO> getProfile(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        UserDTO userDTO = userService.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}
