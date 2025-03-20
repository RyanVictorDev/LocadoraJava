package com.locadora.springboot.renters.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record CreateRenterRequestDTO(
        @NotBlank(message = "O nome não pode estar vazio.") String name,
        @NotBlank(message = "O email não pode estar vazioooo.") @Email(message = "Email inválido.") String email,
        @NotBlank(message = "O telefone não pode estar vazio.") String telephone,
        @NotBlank(message = "O endereço não pode estar vazio.") String address,
        @NotBlank(message = "O cpf não pode estar vazio.") String cpf) {
}
