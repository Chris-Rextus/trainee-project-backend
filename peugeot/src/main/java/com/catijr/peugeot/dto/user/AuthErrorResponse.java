package com.catijr.peugeot.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;

    private String error;

    private String message;

    private String path;

    // common types of errors

    public static AuthErrorResponse invalidCredentials(String path) {
        return AuthErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .message("Invalid email or password")
                .path(path)
                .build();
    }

    public static AuthErrorResponse accountLocked(String path) {
        return AuthErrorResponse.builder()
                .status(423)
                .error("Locked")
                .message("Account is locked. Please try again later")
                .path(path)
                .build();
    }
}