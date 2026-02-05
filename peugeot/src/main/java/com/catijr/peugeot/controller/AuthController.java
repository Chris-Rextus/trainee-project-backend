package com.catijr.peugeot.controller;

import com.catijr.peugeot.dto.user.LoginRequest;
import com.catijr.peugeot.dto.user.LoginResponse;
import com.catijr.peugeot.dto.user.RegisterRequest;
import com.catijr.peugeot.dto.user.UserResponse;
import com.catijr.peugeot.services.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/register
     * Register a new User into our database
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

        log.info("Register request: {}", request);

        UserResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /api/auth/login
     * Authenticate User and returns JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        log.info("Login request: {}", request);

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/auth/health
     * Public health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is up and healthy!");
    }
}