package com.base.base.login.backend.controller;

import com.base.base.login.backend.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Server is running")
                .data("OK")
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/welcome")
    public ResponseEntity<ApiResponse<String>> welcome() {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(true)
                .message("Welcome to Base Login Backend")
                .data("Use /api/auth/register to register and /api/auth/login to login")
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }
}
