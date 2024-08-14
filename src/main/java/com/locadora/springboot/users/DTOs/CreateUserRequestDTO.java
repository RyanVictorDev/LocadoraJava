package com.locadora.springboot.users.DTOs;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank String name,
        @NotBlank  String email,
        @NotBlank  String password,
        @NotBlank String role) {
}