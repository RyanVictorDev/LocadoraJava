package com.locadora.springboot.dashboard.mappers;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.dashboard.DTOs.BooksMoreRented;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookRentMapper {

    @Autowired
    private RentRepository rentRepository;

    public List<BooksMoreRented> toBooksMoreRentedList(List<BookModel> bookList) {
        return bookList.stream()
                .map(this::toBooksMoreRented)
                .sorted((b1, b2) -> Integer.compare(b2.totalRents(), b1.totalRents()))
                .limit(3)
                .collect(Collectors.toList());
    }

    private BooksMoreRented toBooksMoreRented(BookModel book) {
        int rentCount = rentRepository.findAllByBookId(book.getId()).size();
        return new BooksMoreRented(book.getName(), rentCount);
    }
}
