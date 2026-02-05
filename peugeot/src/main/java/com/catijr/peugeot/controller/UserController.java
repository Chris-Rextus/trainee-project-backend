package com.catijr.peugeot.controller;

import com.catijr.peugeot.dto.user.UpdateUserRequest;
import com.catijr.peugeot.dto.user.UserResponse;
import com.catijr.peugeot.entities.User;
import com.catijr.peugeot.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    // GET /users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponse> users = userService.getAllUserResponses();
        return ResponseEntity.ok(users);
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        log.info("Fetching user with ID: {}", id);
        User user = userService.getUserById(id);
        UserResponse response = UserResponse.fromEntity(user);
        return ResponseEntity.ok(response);
    }

    // GET /users/me
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.badRequest().build();
        }

        String userId = (String) authentication.getPrincipal();
        log.info("Getting current user with ID: {}", userId);

        User user = userService.getUserById(UUID.fromString(userId));
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }

    // PUT /users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {

        log.info("Updating user with ID: {}", id);
        User updatedUser = userService.updateUser(id, request);
        UserResponse response = UserResponse.fromEntity(updatedUser);
        return ResponseEntity.ok(response);
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}