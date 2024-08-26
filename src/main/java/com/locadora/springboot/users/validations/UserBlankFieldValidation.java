package com.locadora.springboot.users.validations;

import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.users.DTOs.CreateUserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UserBlankFieldValidation {

    public void validateFields(CreateUserRequestDTO data) {
        if (isNullOrBlank(data.name())) {
            throw new CustomValidationException("The name.");
        }
    }

    private boolean isNullOrBlank(String value){
        return value == null || value.trim().isEmpty();
    }
}
