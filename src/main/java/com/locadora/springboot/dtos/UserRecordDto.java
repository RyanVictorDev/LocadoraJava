package com.locadora.springboot.dtos;

import com.locadora.springboot.models.UserModel;
import jakarta.validation.constraints.NotBlank;

public record UserRecordDto(@NotBlank String name, @NotBlank  String email, @NotBlank UserModel.Role role) {
}