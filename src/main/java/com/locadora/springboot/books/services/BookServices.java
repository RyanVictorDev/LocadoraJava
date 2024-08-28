package com.locadora.springboot.books.services;

import com.locadora.springboot.books.DTOs.CreateBookRequestDTO;
import com.locadora.springboot.books.DTOs.UpdateBookRecordDTO;
import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.books.validations.BookValidation;
import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServices {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    BookValidation bookValidation;

    public ResponseEntity<Void> create(@Valid CreateBookRequestDTO data) {

        bookValidation.validLaunchDate(data);
        bookValidation.validTotalQuantity(data);

        PublisherModel publisher = publisherRepository.findById(data.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));

        BookModel newBook = new BookModel(data.name(), data.author(), data.launchDate(), data.totalQuantity(), publisher);
        bookRepository.save(newBook);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<BookModel> findAll(){
        List<BookModel> books = bookRepository.findAllByIsDeletedFalse();
        if(books.isEmpty()) throw new ModelNotFoundException();
        return books;
    }

    public Optional<BookModel> findById(int id){
        return bookRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateBookRecordDTO updateBookRecordDTO){
        Optional<BookModel> response = bookRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");

        bookValidation.validTotalQuantityUpdate(updateBookRecordDTO);
        bookValidation.validLaunchDateUpdate(updateBookRecordDTO);

        PublisherModel publisher = publisherRepository.findById(updateBookRecordDTO.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Publisher not found"));

        var bookModel = response.get();
        bookModel.setName(updateBookRecordDTO.name());
        bookModel.setAuthor(updateBookRecordDTO.author());
        bookModel.setLaunchDate(updateBookRecordDTO.launchDate());
        bookModel.setTotalQuantity(updateBookRecordDTO.totalQuantity());
        bookModel.setPublisher(publisher);

        bookRepository.save(bookModel);
        return ResponseEntity.status(HttpStatus.OK).body(bookModel);
    }

    public ResponseEntity<Object> delete(int id){
        Optional<BookModel> response = bookRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");

        bookValidation.validDeleteBook(id);

        bookRepository.delete(response.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
    }
}
