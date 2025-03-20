package com.locadora.springboot.users.DTOs;

import com.locadora.springboot.users.models.UserRoleEnum;

public record LoginResponseDTO(String token, UserRoleEnum role, String name, String email) {

}