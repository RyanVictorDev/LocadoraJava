package com.locadora.springboot.publishers.validations;

import com.locadora.springboot.books.models.BookModel;
import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.CustomValidationException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.publishers.services.PublisherServices;
import com.locadora.springboot.renters.DTOs.CreateRenterRequestDTO;
import com.locadora.springboot.rents.models.RentStatusEnum;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherValidationTest {

    @Mock
    PublisherRepository publisherRepository;

    @Mock
    RentRepository rentRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    PublisherServices publisherServices;

    @InjectMocks
    private PublisherValidation publisherValidation;

    private CreatePublisherRequestDTO createPublisherRequestDTO;
    private UpdatePublisherRecordDTO updatePublisherRecordDTO;
    private PublisherModel existingPublisher;

    @BeforeEach
    void setUp(){
        createPublisherRequestDTO = new CreatePublisherRequestDTO("Publisher", "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        updatePublisherRecordDTO = new UpdatePublisherRecordDTO("Publisher", "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        existingPublisher = new PublisherModel(1, "existing publisher", "expublisher@gmail.com", "(85)980809090", "https://publisher.com", false);
    }

    @Test
    void shouldThrowExceptionWhenNameIsInUse(){
        when(publisherRepository.findByNameAndIsDeletedFalse(createPublisherRequestDTO.name())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInUse(){
        when(publisherRepository.findByEmailAndIsDeletedFalse(createPublisherRequestDTO.email())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenTelephoneIsInUse(){
        when(publisherRepository.findByTelephoneAndIsDeletedFalse(createPublisherRequestDTO.telephone())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenSiteIsInUse(){
        when(publisherRepository.findBySiteAndIsDeletedFalse(createPublisherRequestDTO.site())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        CreatePublisherRequestDTO nullNameDTO = new CreatePublisherRequestDTO(null, "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullNameDTO), "O nome não pode estar vazio.");

        CreatePublisherRequestDTO emptyNameDTO = new CreatePublisherRequestDTO("", "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyNameDTO), "O nome não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNullOrEmpty() {
        CreatePublisherRequestDTO nullNameDTO = new CreatePublisherRequestDTO("Publisher", null, "(85)988489035", "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullNameDTO), "O nome não pode estar vazio.");

        CreatePublisherRequestDTO emptyNameDTO = new CreatePublisherRequestDTO("Publisher", "", "(85)988489035", "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyNameDTO), "O nome não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionWhenTelephoneIsNullOrEmpty() {
        CreatePublisherRequestDTO nullNameDTO = new CreatePublisherRequestDTO("Publisher", "publisher@gmail.com", null, "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullNameDTO), "O telefone não pode estar vazio.");

        CreatePublisherRequestDTO emptyNameDTO = new CreatePublisherRequestDTO("Publisher", "publisher@gmail.com", "", "https://publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyNameDTO), "O telefone não pode estar vazio.");
    }

    @Test
    void shouldCreateWhenValidData(){
        when(publisherRepository.findByNameAndIsDeletedFalse(createPublisherRequestDTO.name())).thenReturn(null);
        when(publisherRepository.findByEmailAndIsDeletedFalse(createPublisherRequestDTO.email())).thenReturn(null);
        when(publisherRepository.findByTelephoneAndIsDeletedFalse(createPublisherRequestDTO.telephone())).thenReturn(null);
        when(publisherRepository.findBySiteAndIsDeletedFalse(createPublisherRequestDTO.site())).thenReturn(null);

        publisherValidation.create(createPublisherRequestDTO);
    }

    @Test
    void shouldThrowExceptionUpdatingWithExistingName(){
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findByNameAndIsDeletedFalse(updatePublisherRecordDTO.name())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    @Test
    void shouldThrowExceptionUpdatingWithExistingEmail(){
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findByEmailAndIsDeletedFalse(updatePublisherRecordDTO.email())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    @Test
    void shouldThrowExceptionUpdatingWithExistingTelephone(){
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findByTelephoneAndIsDeletedFalse(updatePublisherRecordDTO.telephone())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    @Test
    void shouldThrowExceptionUpdatingWithExistingSite() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findBySiteAndIsDeletedFalse(updatePublisherRecordDTO.site())).thenReturn(existingPublisher);
        existingPublisher.setSite("https://newsite.com");

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    @Test
    void shouldThrowExceptionUpdateWhenNameIsNullOrEmpty() {
        UpdatePublisherRecordDTO nullNameDTO = new UpdatePublisherRecordDTO(null, "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(nullNameDTO, existingPublisher.getId()), "O nome não pode estar vazio.");

        UpdatePublisherRecordDTO emptyNameDTO = new UpdatePublisherRecordDTO("", "publisher@gmail.com", "(85)988489035", "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(emptyNameDTO, existingPublisher.getId()), "O nome não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionUpdateWhenEmailIsNullOrEmpty() {
        UpdatePublisherRecordDTO nullNameDTO = new UpdatePublisherRecordDTO("Publisher", null, "(85)988489035", "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(nullNameDTO, existingPublisher.getId()), "O nome não pode estar vazio.");

        UpdatePublisherRecordDTO emptyNameDTO = new UpdatePublisherRecordDTO("Publisher", "", "(85)988489035", "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(emptyNameDTO, existingPublisher.getId()), "O nome não pode estar vazio.");
    }

    @Test
    void shouldThrowExceptionUpdateWhenTelephoneIsNullOrEmpty() {
        UpdatePublisherRecordDTO nullNameDTO = new UpdatePublisherRecordDTO("Publisher", "publisher@gmail.com", null, "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(nullNameDTO, existingPublisher.getId()), "O telefone não pode estar vazio.");

        UpdatePublisherRecordDTO emptyNameDTO = new UpdatePublisherRecordDTO("Publisher", "publisher@gmail.com", "", "https://publisher.com");
        assertThrows(NoSuchElementException.class, () -> publisherValidation.update(emptyNameDTO, existingPublisher.getId()), "O telefone não pode estar vazio.");
    }

    @Test
    void shouldUpdateWhenValidData(){
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));

        lenient().when(publisherRepository.findByNameAndIsDeletedFalse(updatePublisherRecordDTO.name())).thenReturn(null);
        lenient().when(publisherRepository.findByEmailAndIsDeletedFalse(updatePublisherRecordDTO.email())).thenReturn(null);
        lenient().when(publisherRepository.findByTelephoneAndIsDeletedFalse(updatePublisherRecordDTO.telephone())).thenReturn(null);
        lenient().when(publisherRepository.findBySiteAndIsDeletedFalse(updatePublisherRecordDTO.site())).thenReturn(null);

        publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId());
    }

    @Test
    void shouldDeletePublisherFailed() {
        var book = new BookModel();
        book.setId(1);
        var publisher = new PublisherModel();
        publisher.setId(1);

        lenient().when(bookRepository.findByPublisherId(publisher.getId())).thenReturn(List.of(book));
        lenient().when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
        lenient().when(rentRepository.existsByBookIdAndStatus(book.getId(), RentStatusEnum.RENTED)).thenReturn(true);

        var exception = assertThrows(CustomValidationException.class, () -> publisherValidation.validDeletePublisher(publisher.getId()));
        assertTrue(exception.getMessage().contains("Não é possível excluir a editora. Existem livros atualmente alugados."));
    }
}