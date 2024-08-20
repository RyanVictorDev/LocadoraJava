package com.locadora.springboot.publishers.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRecordDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotNull int telephone,
        String site) {
}
