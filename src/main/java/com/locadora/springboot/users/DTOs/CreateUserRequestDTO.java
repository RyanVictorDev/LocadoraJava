package com.locadora.springboot.users.DTOs;

import com.locadora.springboot.users.models.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String password,
        @NotNull UserRoleEnum role) {
}
