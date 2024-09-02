package com.locadora.springboot.rents.validation;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentModel;
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

        RenterModel renter = renterRepository.findById(data.renterId()).get();

        if (renter.isDeleted()){
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

        BookModel book = bookRepository.findById(data.bookId()).get();

        if (book.isDeleted()){
            throw new CustomValidationException("Book not found");
        }
    }

    public void validateBookIdUpdate(UpdateRentRecordDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Book not found");
        }
    }

    public void validateDeadLine(CreateRentRequestDTO data){
        if (data.deadLine().isAfter(LocalDate.now().plusDays(30))){
            throw new CustomValidationException("Deadline cannot be more 30 days.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("The deadline cannot be in the past.");
        }
    }

    public void validateDeadLineUpdate(UpdateRentRecordDTO data) {
        if (data.deadLine().isAfter(LocalDate.now().plusDays(30))) {
            throw new CustomValidationException("Deadline cannot be more than 30 days from today.");
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
        if (rentRepository.existsByRenterIdAndBookIdAndStatus(data.renterId(), data.bookId(), RentStatusEnum.RENTED)){
            throw new CustomValidationException("Renter already has this book rented.");
        }
    }

    public void validateRentLate(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndStatus(data.renterId(), RentStatusEnum.LATE)){
            throw new CustomValidationException("Renter has late rent.");
        }
    }

    public void deliveredValidate(int id){
        RentModel rent = rentRepository.findById(id).get();
        if (rent.getStatus() == RentStatusEnum.DELIVERED || rent.getStatus() == RentStatusEnum.DELIVERED_WITH_DELAY || rent.getStatus() == RentStatusEnum.IN_TIME){
            throw new CustomValidationException("Rent is already refunded");
        }
    }

    public void setRentStatus(RentModel rent){
        if (rent.getDevolutionDate() == null){

            if (rent.getDeadLine().isBefore(LocalDate.now())) {
                rent.setStatus(RentStatusEnum.LATE);
                rentRepository.save(rent);
            } else if (rent.getDevolutionDate() == null) {
                rent.setStatus(RentStatusEnum.RENTED);
                rentRepository.save(rent);
            }

        } else {

            if (rent.getDevolutionDate().isAfter(rent.getDeadLine())) {
                rent.setStatus(RentStatusEnum.DELIVERED_WITH_DELAY);
                rentRepository.save(rent);
            } else {
                rent.setStatus(RentStatusEnum.IN_TIME);
                rentRepository.save(rent);
            }
        }
    }
}
