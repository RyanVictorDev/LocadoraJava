package com.locadora.springboot.users.DTOs;

import com.locadora.springboot.users.models.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDTO(
        @NotBlank(message = "The name cannot be empty") String name,
        @NotBlank(message = "Email cannot be null") @Email(message = "Invalid email") String email,
        @NotBlank(message = "Password cannot be empty") String password,
        @NotNull(message = "Role cannot be null") UserRoleEnum role) {
}