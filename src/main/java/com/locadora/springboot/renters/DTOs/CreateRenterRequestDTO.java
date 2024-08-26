package com.locadora.springboot.renters.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateRenterRequestDTO(
        @NotBlank(message = "") String name,
        @NotBlank @Email String email,
        @NotBlank String telephone,
        @NotBlank String address,
        @CPF String cpf) {
}
