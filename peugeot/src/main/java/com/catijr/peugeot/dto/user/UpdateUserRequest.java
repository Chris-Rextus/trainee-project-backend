package com.catijr.peugeot.dto.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Email(message = "Email must be valid")
    //@Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    //@Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    //@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    //@Size(min = 8, message = "Password must be at least 8 characters")
    /*
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
        message = "Password must contain at least one letter and one number"
    )
     */
    private String password;

    // constructor for partial updates (PUT operations)
    public UpdateUserRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }
}