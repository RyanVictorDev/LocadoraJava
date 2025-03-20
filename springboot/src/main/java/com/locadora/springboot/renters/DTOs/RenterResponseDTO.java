package com.locadora.springboot.renters.DTOs;

import lombok.Builder;

@Builder
public record RenterResponseDTO(
        int id,
        String name,
        String email,
        String telephone,
        String address,
        String cpf) {
}
