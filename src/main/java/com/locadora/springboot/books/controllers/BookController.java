package com.locadora.springboot.books.controllers;

import com.locadora.springboot.books.DTOs.BookResponseDTO;
import com.locadora.springboot.books.DTOs.CreateBookRequestDTO;
import com.locadora.springboot.books.DTOs.UpdateBookRecordDTO;
import com.locadora.springboot.books.mappers.BookMapper;
import com.locadora.springboot.books.services.BookServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookServices bookServices;

    @PostMapping("/book")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateBookRequestDTO data){
        return bookServices.create(data);
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookResponseList(bookServices.findAll()));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable(value = "id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookResponse(bookServices.findById(id).get()));
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Object> update(@PathVariable(value="id") int id, @RequestBody @Valid UpdateBookRecordDTO updateBookRecordDTO){
        return bookServices.update(id, updateBookRecordDTO);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value="id") int id){
        return bookServices.delete(id);
    }
}
