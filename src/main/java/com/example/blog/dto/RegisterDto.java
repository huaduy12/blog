package com.example.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotBlank(message = "Name can't empty")
    @Size(min = 8,message = "Name must be 8 characters")
    private String name;
    @NotBlank(message = "Username can't empty")
    @Size(min = 8,message = "Username must be 8 characters")
    private String username;
    @NotBlank(message = "Name can't empty")
    @Email
    private String email;
    @NotBlank(message = "Password can't empty")
    @Size(min = 8,message = "Password must be 8 characters")
    private String password;
}
