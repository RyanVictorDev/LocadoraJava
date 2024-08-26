package com.locadora.springboot.users.validations;

import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidation {

    private final UserRepository userRepository;

    public UserValidation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateEmail(CreateUserRequestDTO data) {
        if (userRepository.findByEmail(data.email()) != null) {
            throw new CustomValidationException("Email already in use.");
        }
    }

    public void validateUpdateEmail(UpdateUserRequestDTO data) {
        if (userRepository.findByEmail(data.email()) != null) {
            throw new CustomValidationException("Email already in use.");
        }
    }
}