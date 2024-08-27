package com.locadora.springboot.books.validations;

import com.locadora.springboot.books.DTOs.CreateBookRequestDTO;
import com.locadora.springboot.books.DTOs.UpdateBookRecordDTO;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class BookValidation {

    @Autowired
    BookRepository bookRepository;

    public void validLaunchDate(CreateBookRequestDTO data){
        if (data.launchDate().isAfter(LocalDate.now())){
            throw new CustomValidationException("Launch Date cannot be in future");
        }
    }

    public void validLaunchDateUpdate(UpdateBookRecordDTO data){
        if (data.launchDate().isAfter(LocalDate.now())){
            throw new CustomValidationException("Launch Date cannot be in future");
        }
    }


    public void validTotalQuantity(CreateBookRequestDTO data){
        if (data.totalQuantity() <= 0){
            throw new CustomValidationException("The total quantity cannot be less than 1");
        }
    }

    public void validTotalQuantityUpdate(UpdateBookRecordDTO data){
        if (data.totalQuantity() <= 0){
            throw new CustomValidationException("The total quantity cannot be less than 1");
        }
    }
}
