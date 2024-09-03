package com.locadora.springboot.users.validations;

import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import com.locadora.springboot.users.DTOs.UpdateUserRequestDTO;
import com.locadora.springboot.users.models.UserModel;
import com.locadora.springboot.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class UserValidation {

    private final UserRepository userRepository;

    public void validateName(CreateUserRequestDTO data){
        if (userRepository.findByName(data.name()) != null){
            throw new CustomValidationException("User name already in use");
        }
    }

    public void validateNameUpdate(UpdateUserRequestDTO data, int id){
        UserModel userModel = userRepository.findById(id).get();

        if (!Objects.equals(userModel.getName(), data.name())){
            if (userRepository.findByName(data.name()) != null){
                throw new CustomValidationException("User name already in use");
            }
        }
    }

    public void validateEmail(CreateUserRequestDTO data) {
        if (userRepository.findByEmail(data.email()) != null) {
            throw new CustomValidationException("Email already in use.");
        }
    }

    public void validateUpdateEmail(UpdateUserRequestDTO data, int id) {
        UserModel userModel = userRepository.findById(id).get();

        if (!Objects.equals(userModel.getEmail(), data.email())){
            if (userRepository.findByEmail(data.email()) != null) {
                throw new CustomValidationException("Email already in use.");
            }
        }
    }
}