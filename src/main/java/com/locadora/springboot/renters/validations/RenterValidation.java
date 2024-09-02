package com.locadora.springboot.renters.validations;

import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.DTOs.CreateRenterRequestDTO;
import com.locadora.springboot.renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class RenterValidation {

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RentRepository rentRepository;

    public void validateEmail(CreateRenterRequestDTO data){
        if (renterRepository.findByEmail(data.email()) != null){
            throw new CustomValidationException("Email alredy in use.");
        }
    }

    public void validateUpdateEmail(UpdateRenterRequestDTO data, int id){
        RenterModel renter = renterRepository.findById(id).get();

        if (!Objects.equals(renter.getEmail(), data.email())){
            if (renterRepository.findByEmail(data.email()) != null) {
                throw new CustomValidationException("Email alredy in use.");
            }
        }
    }

    public void validateCPF(CreateRenterRequestDTO data){
        if (data.cpf() != null && !data.cpf().isBlank()) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            if (!cpfValidator.isValid(data.cpf(), null)) {
                throw new CustomValidationException("Invalid CPF format.");
            }

            if (renterRepository.findByCpf(data.cpf()) != null) {
                throw new CustomValidationException("CPF alredy in use.");
            }
        }
    }

    public void validateCPFUpdate(UpdateRenterRequestDTO data){
        if (data.cpf() != null && !data.cpf().isBlank()) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            if (!cpfValidator.isValid(data.cpf(), null)) {
                throw new CustomValidationException("Invalid CPF format.");
            }

            if (renterRepository.findByCpf(data.cpf()) != null) {
                throw new CustomValidationException("CPF alredy in use.");
            }
        }
    }

    public void validateDeleteRenter(int id){
            if (rentRepository.existsByRenterIdAndStatus(id, RentStatusEnum.RENTED)) {
                throw new CustomValidationException("Cannot delete renter. There are books currently rented out.");
            }
    }
}
