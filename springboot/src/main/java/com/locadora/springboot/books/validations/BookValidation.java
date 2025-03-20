package com.locadora.springboot.books.validations;

import com.locadora.springboot.books.DTOs.CreateBookRequestDTO;
import com.locadora.springboot.books.DTOs.UpdateBookRecordDTO;
import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Component
public class BookValidation {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RentRepository rentRepository;

    @Autowired
    PublisherRepository publisherRepository;

    public void create (CreateBookRequestDTO data){
        validCreateBook(data);
        validLaunchDate(data);
        validTotalQuantity(data);
        validPublisherExist(data);
    }

    public void update (UpdateBookRecordDTO data){
        validTotalQuantityUpdate(data);
        validLaunchDateUpdate(data);
    }

    private void validPublisherExist(CreateBookRequestDTO data){
        PublisherModel publisher = publisherRepository.findById(data.publisherId()).get();

        if (publisher.isDeleted()){
            throw new CustomValidationException("A editora não existe");
        }
    }

    private void validLaunchDate(CreateBookRequestDTO data){
        if (data.launchDate().isAfter(LocalDate.now())){
            throw new CustomValidationException("A data de lançamento não pode ser no futuro.");
        }
    }

    private void validLaunchDateUpdate(UpdateBookRecordDTO data){
        if (data.launchDate().isAfter(LocalDate.now())){
            throw new CustomValidationException("A data de lançamento não pode ser no futuro.");
        }
    }

    private void validTotalQuantity(CreateBookRequestDTO data){
        if (data.totalQuantity() <= 0){
            throw new CustomValidationException("A quantidade total não pode ser inferior a 1.");
        }
    }

    private void validTotalQuantityUpdate(UpdateBookRecordDTO data){
        if (data.totalQuantity() < 0){
            throw new CustomValidationException("A quantidade total não pode ser inferior a 1.");
        }
    }

    public void validDeleteBook(int id){
        boolean hasActiveRent = rentRepository.existsByBookId(id);
        if (hasActiveRent) {
            throw new CustomValidationException("O livro não pode ser excluído porque tem uma locação registrada.");
        }
    }

    private void validCreateBook(CreateBookRequestDTO data){
        List<BookModel> books = bookRepository.findAllByNameAndIsDeletedFalse(data.name());
        if (books != null){
            for (BookModel book: books){
                if (Objects.equals(data.name(), book.getName()) && Objects.equals(data.author(), book.getAuthor()) && Objects.equals(data.publisherId(), book.getPublisher().getId())){
                    throw new CustomValidationException("Este livro já está cadastrado.");
                }
            }
        }
    }
}
