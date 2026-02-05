package com.catijr.peugeot.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @Builder.Default
    private String tokenType = "Bearer";

    @NotBlank
    private String accessToken;

    private LocalDateTime expiresAt;

    private UserResponse user;

    // Success message
    @Builder.Default
    private String message = "Login successful";

    public LoginResponse(String accessToken, UserResponse user) {
        this.accessToken = accessToken;
        this.user = user;
        this.tokenType = "Bearer";
        this.message = "Login successful";
    }
}