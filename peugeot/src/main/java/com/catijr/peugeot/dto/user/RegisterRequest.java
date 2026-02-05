package com.catijr.peugeot.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    //@Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    //@Size(min = 8, message = "Password must be at least 8 characters")
    /*
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number"
    )
     */
    private String password;

    @NotBlank(message = "Name is required")
    //@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    //@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username; // Optional

    // constructor for api compatibility. Must be enforced  at service layer.
    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}