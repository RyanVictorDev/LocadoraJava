package com.locadora.springboot.books.services;

import com.locadora.springboot.books.DTOs.CreateBookRequestDTO;
import com.locadora.springboot.books.DTOs.UpdateBookRecordDTO;
import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.books.validations.BookValidation;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    @Mock
    BookRepository bookRepository;

    @Mock
    BookValidation bookValidation;

    @Mock
    PublisherRepository publisherRepository;

    @Mock
    RentRepository rentRepository;

    @InjectMocks
    BookServices bookServices;

    private CreateBookRequestDTO createBookRequestDTO;
    private CreateBookRequestDTO createBookRequestDTOWithNotExistPublisher;
    private BookModel bookModel;
    private PublisherModel publisherModel;
    private UpdateBookRecordDTO updateBookRecordDTO;

    @BeforeEach
    void setup(){
        publisherModel = new PublisherModel(1, "Publisher", "publisher@gmail.com", "(85)980024922", "https://site.com", false);
        createBookRequestDTO = new CreateBookRequestDTO("Book", "Autor", LocalDate.now(), 3, 1);
        createBookRequestDTOWithNotExistPublisher = new CreateBookRequestDTO("Book", "Autor", LocalDate.now(), 3, 2);
        bookModel = new BookModel(1, "Livro", "Autor", LocalDate.now(), 3, 0, false, publisherModel);
        updateBookRecordDTO = new UpdateBookRecordDTO("Book updated", "Autor", LocalDate.now(), 2, 1);
    }

    @Test
    void shouldCreate(){
        when(bookRepository.save(any(BookModel.class))).thenAnswer(invocation -> {
            BookModel book = invocation.getArgument(0);
            book.setId(1);
            return book;
        });

        when(publisherRepository.findById(createBookRequestDTO.publisherId())).thenReturn(Optional.ofNullable(publisherModel));

        ResponseEntity<Void> response = bookServices.create(createBookRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ArgumentCaptor<BookModel> bookCaptor = ArgumentCaptor.forClass(BookModel.class);
        verify(bookRepository, times(1)).save(bookCaptor.capture());

        BookModel savedBook = bookCaptor.getValue();
        assertEquals(createBookRequestDTO.name(), savedBook.getName());
        assertEquals(createBookRequestDTO.publisherId(), savedBook.getPublisher().getId());
    }

    @Test
    void shouldCreateWithPublisherNotFound(){
        when(publisherRepository.findById(createBookRequestDTOWithNotExistPublisher.publisherId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookServices.create(createBookRequestDTOWithNotExistPublisher));
    }

    @Test
    void shouldFindById(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.ofNullable(bookModel));

        Optional<BookModel> bookResult = bookRepository.findById(bookModel.getId());

        assertTrue(bookResult.isPresent());
        assertEquals("Livro", bookResult.get().getName());
    }

    @Test
    void shouldUpdate(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.ofNullable(bookModel));
        when(publisherRepository.findById(publisherModel.getId())).thenReturn(Optional.ofNullable(publisherModel));

        ResponseEntity<Object> response = bookServices.update(bookModel.getId(), updateBookRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(bookValidation, times(1)).update(updateBookRecordDTO);
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentBook(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = bookServices.update(bookModel.getId(), updateBookRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPublisher(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.ofNullable(bookModel));
        when(publisherRepository.findById(updateBookRecordDTO.publisherId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bookServices.update(bookModel.getId(), updateBookRecordDTO));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentBook(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = bookServices.delete(bookModel.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldDeleteBookSuccesfully(){
        when(bookRepository.findById(bookModel.getId())).thenReturn(Optional.ofNullable(bookModel));

        ResponseEntity<Object> response = bookServices.delete(bookModel.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Livro excluído com sucesso.", response.getBody());
    }
}