package com.locadora.springboot.rents.services;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.repositories.RentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<Void> create(@Valid CreateRentRequestDTO data){
        RenterModel renter = renterRepository.findById(data.renterId())
                .orElseThrow(() -> new IllegalArgumentException("Renter not found"));

        BookModel book = bookRepository.findById(data.bookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        RentModel newRent = new RentModel(renter, book, data.deadLine());
        rentRepository.save(newRent);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<RentModel> findAll(){
        List<RentModel> rents = rentRepository.findAll();
        if (rents.isEmpty()) throw new ModelNotFoundException();
        return rents;
    }

    public Optional<RentModel> findById(int id){
        return rentRepository.findById(id);
    }
}
