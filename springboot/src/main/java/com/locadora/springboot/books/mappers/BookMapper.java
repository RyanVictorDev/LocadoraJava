package com.locadora.springboot.books.mappers;

import com.locadora.springboot.books.DTOs.BookResponseDTO;
import com.locadora.springboot.books.models.BookModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    public List<BookResponseDTO> toBookResponseList(List<BookModel> bookList){
        return bookList.stream().map(this::toBookResponse).toList();
    }

    public BookResponseDTO toBookResponse(BookModel model){
        return BookResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .author(model.getAuthor())
                .launchDate(model.getLaunchDate())
                .totalQuantity(model.getTotalQuantity())
                .publisher(model.getPublisher())
                .totalInUse(model.getTotalInUse())
                .build();
    }
}
