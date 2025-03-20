package com.locadora.springboot.publishers.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CreatePublisherRequestDTO(
        @NotBlank(message = "O nome não pode estar vazio.") String name,
        @NotBlank(message = "O email não pode estar vazio.") @Email(message = "Email inválido.") String email,
        @NotNull(message = "O telefone não pode estar vazio.") String telephone,
        @URL(message = "URL inválida.") String site) {
}
