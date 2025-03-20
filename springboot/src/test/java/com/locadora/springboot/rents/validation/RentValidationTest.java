package com.locadora.springboot.rents.validation;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.renters.models.RenterModel;
import com.locadora.springboot.renters.repositories.RenterRepository;
import com.locadora.springboot.rents.DTOs.CreateRentRequestDTO;
import com.locadora.springboot.rents.DTOs.UpdateRentRecordDTO;
import com.locadora.springboot.rents.models.RentModel;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentValidationTest {

    @Mock
    RenterRepository renterRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    RentRepository rentRepository;

    @InjectMocks
    RentValidation rentValidation;

    private CreateRentRequestDTO createRentRequestDTO;
    private CreateRentRequestDTO createRentRequestWithDeadLineInPass;
    private CreateRentRequestDTO createRentRequestWithDeadLineInFuture;

    private UpdateRentRecordDTO updateRentRecordDTO;
    private UpdateRentRecordDTO updateRentRecordWithDeadLineInPass;
    private UpdateRentRecordDTO updateRentRecordWithDeadLineInFuture;

    private RenterModel renter;
    private BookModel book;
    private RentModel rentModel;

    @BeforeEach
    void setUp(){
        createRentRequestDTO = new CreateRentRequestDTO(1, 1, LocalDate.now());
        createRentRequestWithDeadLineInPass = new CreateRentRequestDTO(1, 1, LocalDate.now().minusMonths(3));
        createRentRequestWithDeadLineInFuture = new CreateRentRequestDTO(1, 1, LocalDate.now().plusMonths(3));

        updateRentRecordDTO = new UpdateRentRecordDTO(1, 1, LocalDate.now().plusDays(3), null);
        updateRentRecordWithDeadLineInPass = new UpdateRentRecordDTO(1, 1, LocalDate.now().minusMonths(3), null);
        updateRentRecordWithDeadLineInFuture = new UpdateRentRecordDTO(1, 1, LocalDate.now().plusMonths(3), null);

        renter = new RenterModel();
        book = new BookModel();
        rentModel = new RentModel(1, renter, book, null, LocalDate.now().plusDays(7), LocalDate.now(), RentStatusEnum.RENTED);
    }

    @Test
    void shouldThrowExceptionWhenRenterDontExist(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenRenterRepeated(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(true);

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenRenterHasALatedRent(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(false);
        when(rentRepository.existsByRenterIdAndStatus(createRentRequestDTO.renterId(), RentStatusEnum.LATE)).thenReturn(true);

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenBookDontExist(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(false);
        when(rentRepository.existsByRenterIdAndStatus(createRentRequestDTO.renterId(), RentStatusEnum.LATE)).thenReturn(false);
        when(bookRepository.findById(createRentRequestDTO.bookId())).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenDeadLineIsInThePass(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(false);
        when(rentRepository.existsByRenterIdAndStatus(createRentRequestDTO.renterId(), RentStatusEnum.LATE)).thenReturn(false);
        when(bookRepository.findById(createRentRequestDTO.bookId())).thenReturn(Optional.ofNullable(book));

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestWithDeadLineInPass));
    }

    @Test
    void shouldThrowExceptionWhenDeadLineIsInTheFuture(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(false);
        when(rentRepository.existsByRenterIdAndStatus(createRentRequestDTO.renterId(), RentStatusEnum.LATE)).thenReturn(false);
        when(bookRepository.findById(createRentRequestDTO.bookId())).thenReturn(Optional.ofNullable(book));

        assertThrows(CustomValidationException.class, () -> rentValidation.create(createRentRequestWithDeadLineInFuture));
    }

    @Test
    void shoulCreateWithValidData(){
        when(renterRepository.findById(createRentRequestDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(createRentRequestDTO.renterId(), createRentRequestDTO.bookId(), RentStatusEnum.RENTED)).thenReturn(false);
        when(rentRepository.existsByRenterIdAndStatus(createRentRequestDTO.renterId(), RentStatusEnum.LATE)).thenReturn(false);
        when(bookRepository.findById(createRentRequestDTO.bookId())).thenReturn(Optional.ofNullable(book));

        rentValidation.create(createRentRequestDTO);
    }

    @Test
    void shouldupdateWhenRenterDontExist(){
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.update(updateRentRecordDTO, rentModel.getId()));
    }

    @Test
    void shouldupdateWhenBookDontExist(){
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(updateRentRecordDTO.bookId())).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.update(updateRentRecordDTO, rentModel.getId()));
    }

    @Test
    void shouldupdateWhenDeadLineIsInThePass(){
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(updateRentRecordDTO.bookId())).thenReturn(Optional.ofNullable(book));
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));

        assertThrows(CustomValidationException.class, () -> rentValidation.update(updateRentRecordWithDeadLineInPass, rentModel.getId()));
    }

    @Test
    void shouldupdateWhenDeadLineIsInTheFuture(){
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(updateRentRecordDTO.bookId())).thenReturn(Optional.ofNullable(book));
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));

        assertThrows(CustomValidationException.class, () -> rentValidation.update(updateRentRecordWithDeadLineInFuture, rentModel.getId()));
    }

    @Test
    void shouldUpdateWithValidData(){
        when(renterRepository.findById(updateRentRecordDTO.renterId())).thenReturn(Optional.ofNullable(renter));
        when(bookRepository.findById(updateRentRecordDTO.bookId())).thenReturn(Optional.ofNullable(book));
        when(rentRepository.findById(rentModel.getId())).thenReturn(Optional.ofNullable(rentModel));

        rentValidation.update(updateRentRecordDTO, rentModel.getId());
    }
}