package com.locadora.springboot.rents.services;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import com.locadora.springboot.rents.validation.RentValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentServicesTest {

    @Mock
    RentRepository rentRepository;

    @Mock
    RenterRepository renterRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    RentValidation rentValidation;

    @InjectMocks
    RentServices rentServices;

    private CreateRentRequestDTO createRentRequestDTO;
    private RentModel rentModel;
    private RenterModel renter;
    private PublisherModel publisher;
    private BookModel book;
    private UpdateRentRecordDTO updateRentRecordDTO;

    @BeforeEach
    void setUp(){
        createRentRequestDTO = new CreateRentRequestDTO(1, 1, LocalDate.now().plusDays(5));
        updateRentRecordDTO = new UpdateRentRecordDTO(1, 1, LocalDate.now().plusDays(4), LocalDate.now());

        renter = new RenterModel(1, "Renter", "renter@gmail.com", "(85)911223344", "Rua Acula", "000.000.000-00", false);
        publisher = new PublisherModel(1, "Publisher", "publisher@gmail.com", "(85)988997766", "https://site.com", false);
        book = new BookModel(1, "Book", "Autor", LocalDate.now(), 3, 0, false, publisher);
        rentModel = new RentModel(1, renter, book, LocalDate.now().plusDays(6), LocalDate.now().plusDays(9), null, RentStatusEnum.RENTED);
    }

    @Test
    void shouldCreate(){
        when(rentRepository.save(any(RentModel.class))).thenAnswer(invocation -> {
            RentModel rent = invocation.getArgument(0);
            rent.setId(1);
            return rent;
        });

        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(createRentRequestDTO.bookId())).thenReturn(Optional.ofNullable(book));

        ResponseEntity<Void> response = rentServices.create(createRentRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void shouldFindById(){
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));

        Optional<RentModel> rent = rentServices.findById(rentModel.getId());

        assertTrue(rent.isPresent());
    }

    @Test
    void shouldDelivered(){
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));
        when(bookRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(book));

        ResponseEntity<Object> response = rentServices.delivered(rentModel.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldDeliveredWithNotExistRent(){
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = rentServices.delivered(rentModel.getId());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldUpdate(){
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(updateRentRecordDTO.bookId())).thenReturn(Optional.ofNullable(book));

        ResponseEntity<Object> response = rentServices.update(rentModel.getId(), updateRentRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldUpdateWithNotExistRent(){
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = rentServices.update(rentModel.getId(), updateRentRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}