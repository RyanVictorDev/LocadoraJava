package com.locadora.springboot.renters.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRenterRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotNull int telephone,
        @NotNull String address,
        int cpf) {
}
