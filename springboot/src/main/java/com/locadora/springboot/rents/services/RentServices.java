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
import com.locadora.springboot.users.models.UserModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
        RenterModel renter = renterRepository.findById(data.renterId()).get();

        BookModel book = bookRepository.findById(data.bookId()).get();

        RentModel newRent = new RentModel(renter, book, data.deadLine());

        rentValidation.create(data);

        newRent.setStatus(RentStatusEnum.RENTED);
        rentRepository.save(newRent);

        rentValidation.validateBookTotalQuantity(book);

        book.setTotalQuantity(book.getTotalQuantity() - 1);
        bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Page<RentModel> findAll(String search, int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (Objects.isNull(search) || Objects.equals(search, "")){
            Page<RentModel> rents = rentRepository.findAll(pageable);
            if (rents.isEmpty()) throw new ModelNotFoundException();

            for (RentModel rent : rents) { rentValidation.setRentStatus(rent); }

            return rents;
        } else {
            Page<RentModel> rentSearch = rentRepository.findAllByRenterNameOrBookName(search, pageable);
            return rentSearch;
        }
    }

    public Page<RentModel> findAllByStatus(String search, int page, String status) {
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (Objects.equals(search, "")) {
            Page<RentModel> rents = rentRepository.findAllByStatus(status, pageable);
            if (rents.isEmpty()) throw new ModelNotFoundException();
            return rents;
        } else {
            return rentRepository.findAllByRenterNameOrBookNameAndStatus(search, status, pageable);
        }
    }

    public List<RentModel> findAllWithoutPagination(String search) {
        if (Objects.isNull(search) || Objects.equals(search, "")) {
            return rentRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        } else {
            return rentRepository.findAllByRenterNameOrBookName(search, Sort.by(Sort.Direction.DESC, "id"));
        }
    }

    public Optional<RentModel> findById(int id){
        return rentRepository.findById(id);
    }

    public ResponseEntity<Object> delivered(int id) {
        Optional<RentModel> optionalRent = rentRepository.findById(id);
        if (optionalRent.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluguel não encontrado"); }

        RentModel rent = optionalRent.get();

        rent.setDevolutionDate(LocalDate.now());

        rentValidation.deliveredValidate(id);
        rentValidation.setRentStatus(rent);

        Optional<BookModel> book = bookRepository.findById(rent.getBook().getId());
        book.get().setTotalQuantity(book.get().getTotalQuantity() + 1);
        bookRepository.save(book.get());

        rentRepository.save(rent);
        return ResponseEntity.status(HttpStatus.OK).body(rent);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateRentRecordDTO updateRentRecordDTO) {
        Optional<RentModel> rentOptional = rentRepository.findById(id);

        if (rentOptional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluguel não encontrado"); }

        int originalBook = rentOptional.get().getBook().getId();

        if (rentOptional.isEmpty()) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aluguel não encontrado"); }

        RenterModel renter = renterRepository.findById(updateRentRecordDTO.renterId()).get();

        BookModel book = bookRepository.findById(updateRentRecordDTO.bookId()).get();

        rentValidation.update(updateRentRecordDTO, id);

        RentModel rentModel = rentOptional.get();
        rentModel.setBook(book);
        rentModel.setRenter(renter);
        rentModel.setDeadLine(updateRentRecordDTO.deadLine());

        if (originalBook != updateRentRecordDTO.bookId()){
            BookModel originalBookModel = bookRepository.findById(originalBook).get();

            rentValidation.validateBookTotalQuantity(book);

            int totalQuantity = originalBookModel.getTotalQuantity();
            originalBookModel.setTotalQuantity(totalQuantity + 1);
            int totalInUse = originalBookModel.getTotalInUse();
            originalBookModel.setTotalInUse(totalInUse - 1);
            bookRepository.save(originalBookModel);

            int totalQuantityBook = book.getTotalQuantity();
            book.setTotalQuantity(totalQuantityBook - 1);
            int totalInUseBook = book.getTotalInUse();
            book.setTotalInUse(totalInUseBook + 1);
            bookRepository.save(book);
        }

        rentRepository.save(rentModel);

        return ResponseEntity.status(HttpStatus.OK).body("Aluguel atualizado com sucesso");
    }
}
