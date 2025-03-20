package com.locadora.springboot.users.DTOs;

import com.locadora.springboot.users.models.UserRoleEnum;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        int id,
        String name,
        String email,
        UserRoleEnum role) {
}
