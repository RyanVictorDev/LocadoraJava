package com.locadora.springboot.renters.validations;

import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
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

    public void create(CreateRenterRequestDTO data){
        validateName(data);
        validateEmail(data);
        validateTelephone(data);
        validateCPF(data);
    }

    public void update(UpdateRenterRequestDTO data, int id){
        validateUpdateName(data);
        validateUpdateEmail(data, id);
        validateUpdateTelephone(data, id);
        validateCPFUpdate(data, id);
    }

    private void validateName(CreateRenterRequestDTO data){
        if (data.name() == "" || data.name() == null){
            throw new CustomValidationException("O nome não pode estar vazio.");
        };
    }

    private void validateUpdateName(UpdateRenterRequestDTO data){
        if (data.name() == "" || data.name() == null){
            throw new CustomValidationException("O nome não pode estar vazio.");
        };
    }

    private void validateEmail(CreateRenterRequestDTO data){
        if (data.email() == "" || data.email() == null){
            throw new CustomValidationException("O email não pode estar vazio.");
        };

        if (renterRepository.findByEmailAndIsDeletedFalse(data.email()) != null){
            throw new CustomValidationException("Este email já está em uso.");
        }
    }

    private void validateUpdateEmail(UpdateRenterRequestDTO data, int id){
        RenterModel renter = renterRepository.findById(id).get();

        if (!Objects.equals(renter.getEmail(), data.email())){
            if (renterRepository.findByEmailAndIsDeletedFalse(data.email()) != null) {
                throw new CustomValidationException("E-mail já em uso.");
            }
        }
    }

    private void validateTelephone(CreateRenterRequestDTO data){
        if (data.telephone() == "" || data.telephone() == null){
            throw new CustomValidationException("O telefone não pode estar vazio.");
        };

        if (renterRepository.findByTelephoneAndIsDeletedFalse(data.telephone()) != null){
            throw new CustomValidationException("Este telefone já está em uso.");
        }
    }

    private void validateUpdateTelephone(UpdateRenterRequestDTO data, int id){
        RenterModel renter = renterRepository.findById(id).get();

        if (!Objects.equals(renter.getTelephone(), data.telephone())){
            if (renterRepository.findByTelephoneAndIsDeletedFalse(data.telephone()) != null) {
                throw new CustomValidationException("Telefone já em uso.");
            }
        }
    }

    private void validateCPF(CreateRenterRequestDTO data){
        if (data.cpf() != null && !data.cpf().isBlank()) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            if (!cpfValidator.isValid(data.cpf(), null)) {
                throw new CustomValidationException("Formato de CPF inválido.");
            }

            if (renterRepository.findByCpfAndIsDeletedFalse(data.cpf()) != null) {
                throw new CustomValidationException("CPF já em uso.");
            }
        }
    }

    private void validateCPFUpdate(UpdateRenterRequestDTO data, int id){
        RenterModel renter = renterRepository.findById(id).get();

        if (data.cpf() != null && !data.cpf().isBlank()) {
            if (!Objects.equals(renter.getCpf(), data.cpf())){
                CPFValidator cpfValidator = new CPFValidator();
                cpfValidator.initialize(null);

                if (!cpfValidator.isValid(data.cpf(), null)) {
                    throw new CustomValidationException("Formato de CPF inválido.");
                }

                if (renterRepository.findByCpfAndIsDeletedFalse(data.cpf()) != null) {
                    throw new CustomValidationException("CPF já em uso.");
                }
            }
        }
    }

    public void validateDeleteRenter(int id){
            if (rentRepository.existsByRenterId(id)) {
                throw new CustomValidationException("Não é possível excluir o locatário. Existem livros ligados a ele.");
            }
    }
}
