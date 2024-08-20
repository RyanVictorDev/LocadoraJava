package com.locadora.springboot.renters.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRenterRequestDTO(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String telephone,
        @NotBlank String address,
        @Pattern(regexp = "\\d{11}", message = "CPF must be 11 digits") String cpf) {
}