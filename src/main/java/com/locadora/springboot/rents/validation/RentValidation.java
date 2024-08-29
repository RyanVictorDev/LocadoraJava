package com.locadora.springboot.rents.validation;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class RentValidation {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RentRepository rentRepository;

    public void validateRenterId(CreateRentRequestDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Renter not found");
        }
    }

    public void validateRenterIdUpdate(UpdateRentRecordDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Renter not found");
        }
    }

    public void validateBookId(CreateRentRequestDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Book not found");
        }
    }

    public void validateBookIdUpdate(UpdateRentRecordDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Book not found");
        }
    }

    public void validateDeadLine(CreateRentRequestDTO data){
        if (data.deadLine().isAfter(data.deadLine().plusDays(30))){
            throw new CustomValidationException("Deadline cannot be more 30 days.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("The deadline cannot be in the past.");
        }
    }

    public void validateDeadLineUpdate(UpdateRentRecordDTO data){
        if (data.deadLine().isAfter(data.deadLine().plusDays(30))){
            throw new CustomValidationException("Deadline cannot be more 30 days.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("The deadline cannot be in the past.");
        }
    }

    public void validateBookTotalQuantity(BookModel data){
        if (data.getTotalQuantity() <= 0){
            throw new CustomValidationException("There are no books available");
        }
    }

    public void validateRentRepeated(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterId(data.renterId())){
            throw new CustomValidationException("Renter already has this book rented.");
        }
    }

    public void validateRentLate(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndStatus(data.renterId(), RentStatusEnum.LATE)){
            throw new CustomValidationException("Renter has late rent.");
        }
    }
}
