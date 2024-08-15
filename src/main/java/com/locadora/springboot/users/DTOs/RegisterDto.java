package com.locadora.springboot.users.DTOs;

import com.locadora.springboot.users.models.UserRoleEnum;

public record RegisterDto(String name, String email, String password, UserRoleEnum role) {
}
