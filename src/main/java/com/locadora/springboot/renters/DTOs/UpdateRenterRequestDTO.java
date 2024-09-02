package com.locadora.springboot.renters.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UpdateRenterRequestDTO(
        @NotBlank(message = "The name cannot be empty") String name,
        @NotBlank(message = "The email cannot be empty") @Email(message = "Invalid email.") String email,
        @NotBlank(message = "The telephone cannot be empty") String telephone,
        @NotBlank(message = "The address cannot be empty") String address,
        String cpf) {
}