package com.catijr.peugeot.services;

import com.catijr.peugeot.dto.user.UpdateUserRequest;
import com.catijr.peugeot.dto.user.UserResponse;
import com.catijr.peugeot.entities.User;
import com.catijr.peugeot.exceptions.DuplicateResourceException;
import com.catijr.peugeot.exceptions.UserNotFoundException;
import com.catijr.peugeot.repository.UserRespository;
import com.catijr.peugeot.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRespository userRespository;
    private final PasswordUtils passwordUtils;

    // GET /users - Get all users
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRespository.findAll();
    }

    // GET /users/{id} - Get user by ID
    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRespository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // PUT /users/{id} - Update user
    public User updateUser(UUID id, UpdateUserRequest request) {
        User user = getUserById(id);

        // Update email if provided and different
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRespository.existsByEmail(request.getEmail())) {
                throw new DuplicateResourceException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        // Update username if provided
        if (request.getUsername() != null) {
            if (!request.getUsername().equals(user.getUsername()) &&
                    userRespository.existsByUsername(request.getUsername())) {
                throw new DuplicateResourceException("Username already taken");
            }
            user.setUsername(request.getUsername());
        }

        // Update name if provided
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        // Update password if provided
        if (request.getPassword() != null) {
            //passwordUtils.validatePassword(request.getPassword());
            user.setPasswordHash(passwordUtils.hashPassword(request.getPassword()));
        }

        return userRespository.save(user);
    }

    // DELETE /users/{id} - Delete user
    public void deleteUser(UUID id) {
        if (!userRespository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRespository.deleteById(id);
    }

    // Helper method to convert User to UserResponse
    public UserResponse convertToResponse(User user) {
        return UserResponse.fromEntity(user);
    }

    // Helper to get all users as responses
    public List<UserResponse> getAllUserResponses() {
        return getAllUsers().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }
}