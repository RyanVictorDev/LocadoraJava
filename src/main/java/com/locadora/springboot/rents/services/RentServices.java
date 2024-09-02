package com.locadora.springboot.rents.services;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import com.locadora.springboot.rents.validation.RentValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RentServices {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RentValidation rentValidation;

    public ResponseEntity<Void> create(@Valid CreateRentRequestDTO data){
        rentValidation.validateRenterId(data);
        rentValidation.validateRentRepeated(data);
        rentValidation.validateRentLate(data);

        RenterModel renter = renterRepository.findById(data.renterId()).get();

        rentValidation.validateBookId(data);

        BookModel book = bookRepository.findById(data.bookId()).get();

        rentValidation.validateDeadLine(data);

        RentModel newRent = new RentModel(renter, book, data.deadLine());
        newRent.setStatus(RentStatusEnum.RENTED);
        rentRepository.save(newRent);

        rentValidation.validateBookTotalQuantity(book);

        book.setTotalQuantity(book.getTotalQuantity() - 1);
        bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<RentModel> findAll() {
        List<RentModel> rents = rentRepository.findAll();
        if (rents.isEmpty()) throw new ModelNotFoundException();

        for (RentModel rent : rents) { rentValidation.setRentStatus(rent); }

        return rents;
    }

    public Optional<RentModel> findById(int id){
        return rentRepository.findById(id);
    }

    public ResponseEntity<Object> delivered(int id) {
        Optional<RentModel> optionalRent = rentRepository.findById(id);
        if (optionalRent.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rent not found"); }

        RentModel rent = optionalRent.get();

        rent.setDevolutionDate(LocalDate.now());

        rentValidation.deliveredValidate(id);
        rentValidation.setRentStatus(rent);

        rentRepository.save(rent);
        return ResponseEntity.status(HttpStatus.OK).body(rent);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateRentRecordDTO updateRentRecordDTO) {
        Optional<RentModel> rentOptional = rentRepository.findById(id);
        if (rentOptional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rent not found"); }

        rentValidation.validateRenterIdUpdate(updateRentRecordDTO);
        RenterModel renter = renterRepository.findById(updateRentRecordDTO.renterId()).get();

        rentValidation.validateBookIdUpdate(updateRentRecordDTO);
        BookModel book = bookRepository.findById(updateRentRecordDTO.bookId()).get();

        rentValidation.validateDeadLineUpdate(updateRentRecordDTO);
        rentValidation.validateBookTotalQuantity(book);

        RentModel rentModel = rentOptional.get();
        rentModel.setBook(book);
        rentModel.setRenter(renter);

        rentRepository.save(rentModel);

        return ResponseEntity.status(HttpStatus.OK).body("Rent updated successfully");
    }
}
