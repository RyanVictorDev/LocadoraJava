package com.locadora.springboot.publishers.services;

import com.locadora.springboot.books.repositories.BookRepository;
import com.locadora.springboot.exceptions.ModelNotFoundException;
import com.locadora.springboot.publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.springboot.publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.springboot.publishers.models.PublisherModel;
import com.locadora.springboot.publishers.repositories.PublisherRepository;
import com.locadora.springboot.publishers.validations.PublisherValidation;
import com.locadora.springboot.rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServicesTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherValidation publisherValidation;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private PublisherServices publisherServices;

    private CreatePublisherRequestDTO createPublisherRequestDTO;
    private PublisherModel publisher;

    @BeforeEach
    void setUp() {
        createPublisherRequestDTO = new CreatePublisherRequestDTO("Editora Teste", "contato@editora.com", "123456789", "https://editora.com");
        publisher = new PublisherModel("Editora Atualizada", "novo@editora.com", "987654321", "https://novaeditora.com");
        publisher.setId(1);

        lenient().when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.of(publisher));
    }

    @Test
    void shouldCreate() {
        when(publisherRepository.save(any(PublisherModel.class))).thenAnswer(invocation -> {
            PublisherModel publisher = invocation.getArgument(0);
            publisher.setId(1);
            return publisher;
        });

        ResponseEntity<Void> response = publisherServices.create(createPublisherRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(publisherValidation, times(1)).create(createPublisherRequestDTO);

        ArgumentCaptor<PublisherModel> publisherCaptor = ArgumentCaptor.forClass(PublisherModel.class);
        verify(publisherRepository, times(1)).save(publisherCaptor.capture());

        PublisherModel savedPublisher = publisherCaptor.getValue();
        assertEquals(createPublisherRequestDTO.name(), savedPublisher.getName());
        assertEquals(createPublisherRequestDTO.email(), savedPublisher.getEmail());
        assertEquals(createPublisherRequestDTO.telephone(), savedPublisher.getTelephone());
        assertEquals(createPublisherRequestDTO.site(), savedPublisher.getSite());
    }

    @Test
    void shouldFindById() {
        int publisherId = 1;
        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));

        Optional<PublisherModel> result = publisherServices.findById(publisherId);

        assertTrue(result.isPresent());
        assertEquals("Editora Atualizada", result.get().getName());
        verify(publisherRepository, times(1)).findById(publisherId);
    }

    @Test
    void shouldThrowModelNotFoundExceptionWhenFindAllWithEmptyResult() {
        int page = 0;
        String search = "";
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        when(publisherRepository.findAllByIsDeletedFalse(pageable)).thenReturn(Page.empty());

        assertThrows(ModelNotFoundException.class, () -> publisherServices.findAll(search, page));
        verify(publisherRepository, times(1)).findAllByIsDeletedFalse(pageable);
    }

    @Test
    void shouldUpdatePublisherSuccessfully() {
        UpdatePublisherRecordDTO updateDTO = new UpdatePublisherRecordDTO(
                "Editora Atualizada",
                "novo@editora.com",
                "987654321",
                "https://novaeditora.com"
        );

        when(publisherRepository.save(any(PublisherModel.class))).thenReturn(publisher);

        ResponseEntity<Object> response = publisherServices.update(publisher.getId(), updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(publisherValidation, times(1)).update(updateDTO, publisher.getId());

        ArgumentCaptor<PublisherModel> publisherCaptor = ArgumentCaptor.forClass(PublisherModel.class);
        verify(publisherRepository, times(1)).save(publisherCaptor.capture());

        PublisherModel savedPublisher = publisherCaptor.getValue();
        assertEquals(updateDTO.name(), savedPublisher.getName());
        assertEquals(updateDTO.email(), savedPublisher.getEmail());
        assertEquals(updateDTO.telephone(), savedPublisher.getTelephone());
        assertEquals(updateDTO.site(), savedPublisher.getSite());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPublisher() {
        UpdatePublisherRecordDTO updateDTO = new UpdatePublisherRecordDTO(
                "Editora Atualizada",
                "novo@editora.com",
                "987654321",
                "https://novaeditora.com"
        );

        when(publisherRepository.findById(publisher.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = publisherServices.update(publisher.getId(), updateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Editora não encontrada.", response.getBody());
        verify(publisherRepository, times(1)).findById(publisher.getId());
        verify(publisherRepository, times(0)).save(any(PublisherModel.class));
    }

    @Test
    void shouldDeletePublisherSuccessfully() {
        int publisherId = 1;
        when(publisherRepository.findById(publisherId)).thenReturn(Optional.of(publisher));

        ResponseEntity<Object> response = publisherServices.delete(publisherId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Editora excluída com sucesso.", response.getBody());
        verify(publisherRepository, times(1)).findById(publisherId);
        verify(publisherRepository, times(1)).save(publisher);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPublisher() {
        int publisherId = 1;
        when(publisherRepository.findById(publisherId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = publisherServices.delete(publisherId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Editora não encontrada.", response.getBody());
        verify(publisherRepository, times(1)).findById(publisherId);
        verify(publisherRepository, times(0)).delete(any(PublisherModel.class));
    }
}
