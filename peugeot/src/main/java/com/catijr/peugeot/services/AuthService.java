package com.catijr.peugeot.services;

import com.catijr.peugeot.dto.user.LoginRequest;
import com.catijr.peugeot.dto.user.LoginResponse;
import com.catijr.peugeot.dto.user.RegisterRequest;
import com.catijr.peugeot.dto.user.UserResponse;
import com.catijr.peugeot.entities.User;
import com.catijr.peugeot.exceptions.DuplicateResourceException;
import com.catijr.peugeot.repository.UserRespository;
import com.catijr.peugeot.security.JwtService;
import com.catijr.peugeot.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRespository userRespository;
    private final PasswordUtils passwordUtils;
    private final JwtService jwtService;

    // POST /auth/register - Register new user
    public UserResponse register(RegisterRequest request) {

        if (userRespository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        // Check if username already exists (if provided)
        if (request.getUsername() != null &&
                userRespository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already taken");
        }

        // Create and save user
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .username(request.getUsername())
                .passwordHash(passwordUtils.hashPassword(request.getPassword()))
                .build();

        User savedUser = userRespository.save(user);

        return UserResponse.fromEntity(savedUser);
    }

    // POST /auth/login - Login user
    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRespository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        // Verify password
        if (!passwordUtils.verifyPassword(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Build response
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .user(UserResponse.fromEntity(user))
                .message("Login successful")
                .build();
    }
}