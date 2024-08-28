package com.locadora.springboot.renters.validations;

import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.DTOs.CreateRenterRequestDTO;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.springboot.renters.repositories.RenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RenterValidation {

    @Autowired
    private RenterRepository renterRepository;

    public void validateEmail(CreateRenterRequestDTO data){
        if (renterRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("Email alredy in use.");
        }
    }

    public void validateUpdateEmail(UpdateRenterRequestDTO data){
        if (renterRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("Email alredy in use.");
        }
    }

    public void validateCPF(CreateRenterRequestDTO data){
        if (renterRepository.findByCpf(data.cpf()) != null){
            throw new CustomValidationException("CPF alredy in use.");
        }
    }
}
